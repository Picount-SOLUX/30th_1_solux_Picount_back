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

		// log.info("* 인증 불필요 엔드포인트 확인");
		for (String whitelink : whitelist) {
			if (whitelink.equals(path)) {
				filterChain.doFilter(request, response);
				return;
			}
		}
		String accessToken = resolveToken(request);
		// log.info("* [JwtTokenAuthFilter] accessToken : " + accessToken);

		// log.info("* 토큰이 없거나 유효하지 않은지 확인");
		if (accessToken == null || !jwtTokenProvider.validToken(accessToken)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 없거나 유효하지 않습니다.");
			return;

		}

		// log.info("* 토큰에서 이메일 확인");
		String email = jwtTokenProvider.getEmail(accessToken);
		UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		// log.info("* 인증객체 저장");
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request) {
		String header = request.getHeader(AUTHORIZATION_HEADER);

		boolean tokenFound = StringUtils.hasText(header) && header.startsWith(TOKEN_PREFIX);
		if (tokenFound) {
			return header.substring(TOKEN_PREFIX.length());
		}
		return null;
	}
}