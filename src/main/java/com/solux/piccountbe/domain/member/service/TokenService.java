package com.solux.piccountbe.domain.member.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solux.piccountbe.config.jwt.JwtTokenProvider;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.member.entity.Token;
import com.solux.piccountbe.domain.member.repository.TokenRepository;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class TokenService {

	private final JwtTokenProvider jwtTokenProvider;
	private final TokenRepository tokenRepository;

	public void createOrUpdateRefreshToken(String token, Member member, Long expired) {

		LocalDateTime expiresAt = LocalDateTime.now().plus(Duration.ofMillis(expired));

		tokenRepository.findByMember(member)
			.ifPresentOrElse(
				existing -> {
					existing.setRefreshToken(token);
					existing.setExpiresAt(expiresAt);
				},
				() -> {
					Token newToken = Token.builder()
						.refreshToken(token)
						.member(member)
						.expiresAt(expiresAt)
						.build();
					tokenRepository.save(newToken);
				}
			);
	}

	@Transactional(readOnly = true)
	public String createNewAccessToken(String refreshToken, Long expired) {
		//토큰 유효성 검사 실패
		if (!jwtTokenProvider.validToken(refreshToken)) {
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}
		Member member = tokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN))
			.getMember();

		return jwtTokenProvider.makeToken(member, expired);
	}

}
