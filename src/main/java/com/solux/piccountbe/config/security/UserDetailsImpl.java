package com.solux.piccountbe.config.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.solux.piccountbe.domain.member.entity.Member;

public class UserDetailsImpl implements UserDetails {
	private final Member member;

	public UserDetailsImpl(final Member member) {
		this.member = member;
	}

	public Member getMember() {
		return member;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public String getPassword() {
		return member.getPassword();
	}

	@Override
	public String getUsername() {
		return member.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// 만료 확인
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// 잠금 확인
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// 패스워드 만료 확인
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
