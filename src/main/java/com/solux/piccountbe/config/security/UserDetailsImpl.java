package com.solux.piccountbe.config.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.solux.piccountbe.domain.member.entity.Member;

public class UserDetailsImpl implements UserDetails, OAuth2User {
	private final Member member;
	private Map<String, Object> attributes;

	public UserDetailsImpl(final Member member) {
		this.member = member;
	}

	public Member getMember() {
		return member;
	}

	public static UserDetailsImpl create(Member member, Map<String, Object> attributes) {
		UserDetailsImpl principal = new UserDetailsImpl(member);
		principal.setAttributes(attributes);
		return principal;
	}

	// OAuth2User
	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String getName() {
		return member.getMemberId().toString();
	}

	//UserDetails
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
