package com.solux.piccountbe.config.jwt;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

	@Value("${jwt.access-expired}")
	private long jwtAccessExpired;

	@Value("${jwt.refresh-expired}")
	private long jwtRefreshExpired;

	@Value("${jwt.secret}")
	private String jwtSecret;

	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	public String makeRefreshToken(Member member) {
		return makeToken(member, jwtRefreshExpired);
	}

	public String makeAccessToken(Member member) {
		return makeToken(member, jwtAccessExpired);
	}

	public String makeToken(Member member, Long expired) {
		return Jwts.builder()
			.setSubject(member.getEmail())
			.claim("memberId", member.getMemberId())
			.claim("tokenVersion", member.getTokenVersion())
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + expired))
			.signWith(hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)), signatureAlgorithm)
			.compact();
	}

	public boolean validToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
				.build()
				.parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException | SignatureException e) {
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		} catch (UnsupportedJwtException e) {
			throw new CustomException(ErrorCode.NOT_SUPPORTED_TOKEN);
		} catch (IllegalArgumentException e) {
			throw new CustomException(ErrorCode.FALSE_TOKEN);
		} catch (ExpiredJwtException e) {
			throw new CustomException(ErrorCode.TOKEN_EXPIRATION);
		}
	}

	public String getEmail(String token) {
		Claims claims = getClaims(token);
		return claims.getSubject();
	}

	public Integer getTokenVersion(String token) {
		Claims claims = getClaims(token);
		return claims.get("tokenVersion", Integer.class);
	}

	public Claims getClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
			.build()
			.parseClaimsJws(token)
			.getBody();
	}
}
