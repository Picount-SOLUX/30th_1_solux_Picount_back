package com.solux.piccountbe.domain.member.service;

import com.solux.piccountbe.config.S3.S3UploadService;
import com.solux.piccountbe.config.jwt.JwtTokenProvider;
import com.solux.piccountbe.domain.member.GenerateRandomCode;
import com.solux.piccountbe.domain.member.dto.*;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.member.entity.MemberGroupType;
import com.solux.piccountbe.domain.member.entity.Provider;
import com.solux.piccountbe.domain.member.repository.MemberRepository;
import com.solux.piccountbe.global.DefaultImageUtil;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TokenService tokenService;
    private final S3UploadService s3UploadService;

    private final DefaultImageUtil defaultImageUtil;
    private final JwtTokenProvider jwtTokenProvider;

    public ProfileResponseDto signup(SignupRequestDto signupRequestDto) {
        boolean isEmailExists = memberRepository.existsByEmail(signupRequestDto.getEmail());
        if (isEmailExists) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATED);
        }
        boolean isNicknameExists = memberRepository.existsByNickname(signupRequestDto.getNickname());
        if (isNicknameExists) {
            throw new CustomException(ErrorCode.NICKNAME_DUPLICATED);
        }

        String password = signupRequestDto.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        String defaultImageUrl = defaultImageUtil.getDefaultProfileImageUrl();
        String friendCode;
        do {
            friendCode = GenerateRandomCode.generateRandomCode();
        } while (memberRepository.existsByFriendCode(friendCode));
        Integer tokenVersion = 1;
        boolean withDraw = false;
        boolean isMainVisible = false;

        Member member = Member.builder()
                .provider(Provider.EAMIL)
                .email(signupRequestDto.getEmail())
                .password(encodedPassword)
                .nickname(signupRequestDto.getNickname())
                .profileImageUrl(defaultImageUrl)
                .friendCode(friendCode)
                .memberGroupType(MemberGroupType.STUDENT_UNIV)
                .tokenVersion(tokenVersion)
                .withdraw(withDraw)
                .isMainVisible(isMainVisible)
                .build();

        memberRepository.save(member);

        return ProfileResponseDto.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        Member member = memberRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_EMAIL_NOT_FOUND));

        if (member.getWithdraw()) {
            throw new CustomException(ErrorCode.USER_DELETED);
        }

        if (member.getProvider().equals(Provider.KAKAO)) {
            throw new CustomException(ErrorCode.MEMBER_OAUTH_MISMATCH);
        }

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtTokenProvider.makeAccessToken(member);
        String refreshToken = jwtTokenProvider.makeRefreshToken(member);

        tokenService.createOrUpdateRefreshToken(refreshToken, member);

        LoginResponseDto loginResponseDto = new LoginResponseDto(accessToken, refreshToken);
        return loginResponseDto;
    }

    public LoginResponseDto refresh(String refreshToken) {

        jwtTokenProvider.validToken(refreshToken);
        String email = jwtTokenProvider.getEmail(refreshToken);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_EMAIL_NOT_FOUND));
        Integer tokenVersion = jwtTokenProvider.getTokenVersion(refreshToken);

        if (!tokenVersion.equals(member.getTokenVersion())) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String newAccessToken = jwtTokenProvider.makeAccessToken(member);
        String newRefreshToken = jwtTokenProvider.makeRefreshToken(member);

        tokenService.createOrUpdateRefreshToken(newRefreshToken, member);

        LoginResponseDto loginResponseDto = new LoginResponseDto(newAccessToken, newRefreshToken);
        return loginResponseDto;
    }

    public Long checkEmail(String email) {

        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_EMAIL_NOT_FOUND))
                .getMemberId();
    }

    public void findAccount(Long memberId, String password) {
        Member member = getMemberById(memberId);
        String encodedPassword = passwordEncoder.encode(password);
        member.memberPasswordUpdate(encodedPassword);
    }

    public void logout(Long memberId) {
        Member member = getMemberById(memberId);
        member.plusTokenVersion();
    }

    public void withdraw(Long memberId) {
        logout(memberId);
        Member member = getMemberById(memberId);
        member.withdraw();
    }

    public void updatePassword(Long memberId, String prePassword, String newPassword) {
        Member member = getMemberById(memberId);
        if (!passwordEncoder.matches(prePassword, member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        String password = passwordEncoder.encode(newPassword);
        if (passwordEncoder.matches(prePassword, password)) {
            throw new CustomException(ErrorCode.SAME_PASSWORD);
        }

        member.memberPasswordUpdate(password);
    }

    public ProfileResponseDto getProfile(Member member) {
        ProfileResponseDto profileResponseDto = new ProfileResponseDto(
                member.getMemberId(),
                member.getEmail(),
                member.getNickname(),
                member.getMemberGroupType(),
                member.getMemberGroupType().getLabel(),
                member.getIntro() != null ? member.getIntro() : "",
                member.getFriendCode(),
                member.getProfileImageUrl()
        );
        return profileResponseDto;
    }

    public ProfileResponseDto updateProfile(Long memberId, MultipartFile profileImage, ProfileUpdateRequestDto profileUpdateRequestDto) throws IOException {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!member.getNickname().equals(profileUpdateRequestDto.getNickname())) {
            if (memberRepository.existsByNickname(profileUpdateRequestDto.getNickname())) {
                throw new CustomException(ErrorCode.NICKNAME_DUPLICATED);
            }
        }
        member.memberProfileUpdate(
                profileUpdateRequestDto.getNickname(),
                profileUpdateRequestDto.getIntro()
        );

        String defaultImageUrl = defaultImageUtil.getDefaultProfileImageUrl();

        if (profileImage != null && !profileImage.isEmpty()) {

            String profileImageUrl = s3UploadService.saveFile(profileImage);

            String oldUrl = member.getProfileImageUrl();
            member.memberProfileImageUrlUpdate(profileImageUrl);

            if (oldUrl != null && !oldUrl.equals(defaultImageUrl)) {
                String oldFileName = oldUrl.substring(oldUrl.lastIndexOf("/") + 1);
                s3UploadService.deleteImage(oldFileName);
            }
        }

        return ProfileResponseDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .intro(member.getIntro())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }

    public ProfileResponseDto updateMemberGroupType(Long memberId, MemberGroupType memberGroupType) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        member.memberGroupTypeUpdate(memberGroupType);

        return ProfileResponseDto.builder()
                .memberId(member.getMemberId())
                .memberGroupType(memberGroupType)
                .memberGroupTypeLabel(memberGroupType.getLabel())
                .build();
    }


    public Member findByFriendCode(String friendCode) {
        return memberRepository.findByFriendCode(friendCode)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional
    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    // 포인트 추가
    public void addPoint(Member member, long amount) {
        member.addPoint(amount);
        memberRepository.save(member);
    }

    // 공개 여부
    public void updateMainVisibility(Long memberId, boolean isMainVisible) {
        Member member = getMemberById(memberId);
        member.updateMainVisible(isMainVisible);
        log.info("공개 여부 수정 완료: {}", member.getIsMainVisible());
    }

    public String getFriendCode(Member member) {
        return member.getFriendCode();
    }

}