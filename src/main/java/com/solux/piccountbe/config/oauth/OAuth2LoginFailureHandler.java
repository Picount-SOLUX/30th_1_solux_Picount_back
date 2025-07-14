package com.solux.piccountbe.config.oauth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {
	@Value("${callback-oauth.frontend-redirect-uri}")
	private String redirectUri;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException exception) throws IOException {
		String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
			.queryParam("error", exception.getLocalizedMessage())
			.build().toUriString();
		response.sendRedirect(targetUrl);
	}
}
