package com.solux.piccountbe.config.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.solux.piccountbe.config.jwt.JwtTokenAuthenticationFilter;
import com.solux.piccountbe.config.oauth.OAuth2LoginFailureHandler;
import com.solux.piccountbe.config.oauth.OAuth2LoginSuccessHandler;
import com.solux.piccountbe.domain.member.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(SecurityProperties.class)
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final UserDetailsServiceImpl userDetailsServiceImpl;
	private final SecurityProperties securityProperties;
	private final JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter;

	private final CustomOAuth2UserService customOAuth2UserService;
	private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

	//특정 http 요청에 대한 웹 기반 보안 구성
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
			.authorizeHttpRequests(auth -> //인증/인가 설정
				auth.requestMatchers(securityProperties.getWhitelist().toArray(new String[0])).permitAll()
					.anyRequest().authenticated()
			)
			.oauth2Login(oauth -> oauth
				// 추후 로그인 페이지는 프론트 담당
				.loginPage("/login.html")
				// 카카오 인가 요청 URL
				.authorizationEndpoint(ae -> ae
					.baseUri("/login/oauth2/authorization")
				)
				// 카카오가 보내준 code 받을 API
				.redirectionEndpoint(re -> re
					.baseUri("/api/members/social/*")
				)
				// 토큰 받은 뒤 사용자 정보 로드
				.userInfoEndpoint(ui -> ui
					.userService(customOAuth2UserService)
				)
				// 로그인 성공 뒤 JWT 만들어서 프론트로 리다이렉트
				.successHandler(oAuth2LoginSuccessHandler)
				.failureHandler(oAuth2LoginFailureHandler)
			)
			.sessionManagement(
				session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.addFilterBefore(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// 인증 관리자 설정
	@Bean
	public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) throws Exception {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsServiceImpl);
		authProvider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(authProvider);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}