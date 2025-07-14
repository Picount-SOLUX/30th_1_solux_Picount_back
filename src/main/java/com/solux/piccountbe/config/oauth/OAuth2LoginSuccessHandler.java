package com.solux.piccountbe.config.oauth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.solux.piccountbe.config.jwt.JwtTokenProvider;
import com.solux.piccountbe.config.security.UserDetailsImpl;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.member.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtTokenProvider jwtTokenProvider;
	private final TokenService tokenService;

	@Value("${callback-oauth.frontend-redirect-uri}")
	private String redirectUri;

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication) throws IOException {

		UserDetailsImpl user = (UserDetailsImpl)authentication.getPrincipal();
		Member member = user.getMember();

		String accessToken = jwtTokenProvider.makeAccessToken(member);
		String refreshToken = jwtTokenProvider.makeRefreshToken(member);

		tokenService.createOrUpdateRefreshToken(refreshToken, member);

		// 리다이렉트 URI에 토큰 붙여서 보내기
		String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
			.queryParam("access_token", accessToken)
			.queryParam("refresh_token", refreshToken)
			.build().toUriString();

		response.sendRedirect(targetUrl);
	}
}
