package com.solux.piccountbe.domain.member.entity;

public enum Provider {
	EAMIL("이메일"),
	KAKAO("카카오");

	private final String label;

	Provider(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
