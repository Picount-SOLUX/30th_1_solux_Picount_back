package com.solux.piccountbe.domain.member.service;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.solux.piccountbe.config.jwt.JwtTokenProvider;
import com.solux.piccountbe.domain.member.dto.LoginRequestDto;
import com.solux.piccountbe.domain.member.dto.LoginResponseDto;
import com.solux.piccountbe.domain.member.dto.SignupRequestDto;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.member.entity.Provider;
import com.solux.piccountbe.domain.member.repository.MemberRepository;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	private final TokenService tokenService;

	private static final String CODE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final int CODE_LEN = 8;
	private final SecureRandom random = new SecureRandom();
	private final JwtTokenProvider jwtTokenProvider;

	@Value("${jwt.access-expired}")
	private Long jwtAccessExpired;

	@Value("${jwt.refresh-expired}")
	private Long jwtRefreshExpired;

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
		String defaultImageUrl = "images/default-member-profile.jpg";
		String friendCode;
		do {
			friendCode = generateRandomCode();
		} while (memberRepository.existsByFriendCode(friendCode));
		boolean withDraw = false;

		Member member = Member.builder()
			.provider(Provider.EAMIL)
			.email(signupRequestDto.getEmail())
			.password(encodedPassword)
			.nickname(signupRequestDto.getNickname())
			.profileImageUrl(defaultImageUrl)
			.gender(signupRequestDto.getGender())
			.friendCode(friendCode)
			.age(signupRequestDto.getAge())
			.withdraw(withDraw)
			.build();

		memberRepository.save(member);
	}

	public LoginResponseDto login(LoginRequestDto loginRequestDto) {

		Member member = memberRepository.findByEmail(loginRequestDto.getEmail())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_EMAIL_NOT_FOUND));

		if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD);
		}

		String accessToken = jwtTokenProvider.makeToken(member, jwtAccessExpired);
		String refreshToken = jwtTokenProvider.makeToken(member, jwtRefreshExpired);

		tokenService.createOrUpdateRefreshToken(refreshToken, member, jwtRefreshExpired);

		LoginResponseDto loginResponseDto = new LoginResponseDto(accessToken, refreshToken);
		return loginResponseDto;
	}

	public Member findByFriendCode(String friendCode) {
		return memberRepository.findByFriendCode(friendCode)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
	}

	private String generateRandomCode() {
		StringBuilder sb = new StringBuilder(CODE_LEN);
		for (int i = 0; i < CODE_LEN; i++) {
			sb.append(CODE_CHARS.charAt(random.nextInt(CODE_CHARS.length())));
		}
		return sb.toString();
	}

}