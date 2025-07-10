package com.solux.piccountbe.domain.member.service;

import java.security.SecureRandom;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.solux.piccountbe.domain.member.dto.SignupRequestDto;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.member.entity.Provider;
import com.solux.piccountbe.domain.member.repository.MemberRepository;
import com.solux.piccountbe.domain.member.repository.TokenRepository;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	private final TokenRepository tokenRepository;

	private static final String CODE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final int CODE_LEN = 8;
	private final SecureRandom random = new SecureRandom();

	@Transactional
	public void signup(SignupRequestDto signupRequestDto) {
		boolean isEmailExists = memberRepository.existsByEmail(signupRequestDto.getEmail());
		if (isEmailExists) {
			throw new CustomException(ErrorCode.USER_EMAIL_NOT_FOUND);
		}
		boolean isNicknameExists = memberRepository.existsByNickname(signupRequestDto.getNickname());
		if (isNicknameExists) {
			throw new CustomException(ErrorCode.USER_NICKNAME_NOT_FOUND);
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

	private String generateRandomCode() {
		StringBuilder sb = new StringBuilder(CODE_LEN);
		for (int i = 0; i < CODE_LEN; i++) {
			sb.append(CODE_CHARS.charAt(random.nextInt(CODE_CHARS.length())));
		}
		return sb.toString();
	}

}