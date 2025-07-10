package com.solux.piccountbe.config.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.solux.piccountbe.config.security.SecurityProperties;
import com.solux.piccountbe.config.security.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityProperties.class)
@Slf4j
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

	private final UserDetailsServiceImpl userDetailsServiceImpl;
	private final JwtTokenProvider jwtTokenProvider;
	private final SecurityProperties securityProperties;

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String TOKEN_PREFIX = "Bearer ";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String path = request.getRequestURI();
		List<String> whitelist = securityProperties.getWhitelist();

		String authHeader = request.getHeader(AUTHORIZATION_HEADER);
		String token = getAccessToken(authHeader);

		// 인증불필요 엔드포인트
		for (String whitelink : whitelist) {
			if (whitelink.equals(path)) {
				filterChain.doFilter(request, response);
				return;
			}
		}

		// 토큰이 없거나 유효하지 않음
		if (token == null || !jwtTokenProvider.validToken(token)) {

			filterChain.doFilter(request, response);
			return;
		}

		String email = jwtTokenProvider.getEmail(token);
		UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(token);
		// 이메일 해당되는 사용자 찾기
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		// SecurityContext에 인증객체 저장
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}

	// request 헤더에서 토큰값 추출
	private String getTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

		boolean tokenFound = StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX);
		if (tokenFound) {
			return bearerToken.substring(TOKEN_PREFIX.length());
		}
		return null;
	}

	private String getAccessToken(String authHeader) {
		if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
			return authHeader.substring(TOKEN_PREFIX.length());
		}
		return null;
	}
}