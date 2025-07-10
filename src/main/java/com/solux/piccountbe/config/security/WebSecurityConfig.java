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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.solux.piccountbe.config.jwt.JwtTokenAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(SecurityProperties.class)
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final UserDetailsServiceImpl userDetailsServiceImpl;
	private final SecurityProperties securityProperties;
	private final JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter;

	//특정 http 요청에 대한 웹 기반 보안 구성
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
			.authorizeHttpRequests(auth -> //인증/인가 설정
				auth.requestMatchers(securityProperties.getWhitelist().toArray(new String[0])).permitAll()
					.anyRequest().authenticated()
			)
			.sessionManagement(
				session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// 인증 관리자 설정
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception {
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
