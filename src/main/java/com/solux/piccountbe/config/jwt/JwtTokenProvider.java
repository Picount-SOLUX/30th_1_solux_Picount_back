package com.solux.piccountbe.config.jwt;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.solux.piccountbe.domain.member.entity.Member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

	// 환경변수 설정 필요
	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.access-expired}")
	private Long jwtAccessExpired;

	@Value("${jwt.refresh-expired}")
	private Long jwtRefreshExpired;

	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	public String generateToken(Member member, Duration expiredAt) {
		Date now = new Date();
		return makeToken(new Date(now.getTime() + expiredAt.toMillis()), member);
	}

	public String makeToken(Date expiry, Member member) {
		Key key = hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
		return Jwts.builder()
			// .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.setSubject(member.getEmail())
			.claim("memberId", member.getMemberId())
			.setIssuedAt(new Date())
			.setExpiration(expiry)
			// .signWith(signatureAlgorithm, jwtSecret)
			.signWith(key, signatureAlgorithm)
			.compact();
	}

	public boolean validToken(String token) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			return true;
		} catch (Exception e) { ////////////////
			return false;
		}
	}

	public String getEmail(String token) {
		Claims claims = getClaims(token);
		return claims.get("email", String.class);
	}

	private Claims getClaims(String token) {
		return Jwts.parser()
			.setSigningKey(jwtSecret)
			.parseClaimsJws(token)
			.getBody();
	}
}
