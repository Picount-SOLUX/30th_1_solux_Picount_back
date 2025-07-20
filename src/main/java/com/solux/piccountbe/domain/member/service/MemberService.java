package com.solux.piccountbe.domain.member.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.solux.piccountbe.config.jwt.JwtTokenProvider;
import com.solux.piccountbe.domain.member.GenerateRandomCode;
import com.solux.piccountbe.domain.member.dto.LoginRequestDto;
import com.solux.piccountbe.domain.member.dto.LoginResponseDto;
import com.solux.piccountbe.domain.member.dto.ProfileResponseDto;
import com.solux.piccountbe.domain.member.dto.ProfileUpdateRequestDto;
import com.solux.piccountbe.domain.member.dto.SignupRequestDto;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.member.entity.MemberGroupType;
import com.solux.piccountbe.domain.member.entity.Provider;
import com.solux.piccountbe.domain.member.repository.MemberRepository;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class MemberService {

	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	private final TokenService tokenService;

	private final JwtTokenProvider jwtTokenProvider;

	@Value("${upload.default_profile_image}")
	private String defaultProfileImage;

	@Transactional
	public void signup(SignupRequestDto signupRequestDto) {
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
		String defaultImageUrl = defaultProfileImage;
		String friendCode;
		do {
			friendCode = GenerateRandomCode.generateRandomCode();
		} while (memberRepository.existsByFriendCode(friendCode));
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
			.withdraw(withDraw)
			.isMainVisible(isMainVisible)
			.build();

		memberRepository.save(member);
	}

	public LoginResponseDto login(LoginRequestDto loginRequestDto) {

		Member member = memberRepository.findByEmail(loginRequestDto.getEmail())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_EMAIL_NOT_FOUND));

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

	public void updateProfile(Long memberId, MultipartFile profileImage, ProfileUpdateRequestDto profileUpdateRequestDto) {

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

		if (profileImage != null && !profileImage.isEmpty()) {
			String uploadDir = "./uploads/profileImages";
			String newFileName = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();

			// 현재 디렉토리
			Path cwd = Paths.get("").toAbsolutePath();
			Path uploadPath = cwd.resolve(uploadDir).normalize();
			try {
				Files.createDirectories(uploadPath);
			} catch (IOException e) {
				throw new CustomException(ErrorCode.MEMBER_IMAGE_DIRECTORY_FAILED);
			}

			// 이전 업로드한 파일 삭제
			String oldUrl = member.getProfileImageUrl();
			if (oldUrl != null && !oldUrl.equals(defaultProfileImage)) {
				// oldUrl에서 filename 추출
				String oldFileName = Paths.get(oldUrl).getFileName().toString();
				Path oldFile = uploadPath.resolve(oldFileName);
				try {
					Files.deleteIfExists(oldFile);
				} catch (IOException e) {
					// 삭제 실패돼도 이미지파일 변경 가능하도록
					log.warn("이전 프로필 이미지 삭제 실패: {}", oldFile, e);
				}
			}

			Path target = uploadPath.resolve(newFileName);

			try {
				profileImage.transferTo(target.toFile());
			} catch (IOException e) {
				throw new CustomException(
					ErrorCode.MEMBER_IMAGE_UPLOAD_FAILED);
			}

			member.memberProfileImageUrlUpdate("/images/profileImages/" + newFileName);
		}

	}

	public void updateMemberGroupType(Long memberId, MemberGroupType memberGroupType) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		member.memberGroupTypeUpdate(memberGroupType);
	}

	public void updateEmail(Long memberId, String email) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		if (!member.getEmail().equals(email)) {
			if (memberRepository.existsByEmail(email)) {
				throw new CustomException(ErrorCode.EMAIL_DUPLICATED);
			}
		}
		Member updateMember = member.memberEmailUpdate(email);
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

}