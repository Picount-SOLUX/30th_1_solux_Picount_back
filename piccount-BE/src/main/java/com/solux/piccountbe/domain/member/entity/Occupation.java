package com.solux.piccountbe.domain.member.entity;

public enum Occupation {
	STUDENT("학생"),
	EMPLOYEE("직장인"),
	HOMEMAKER("전업주부"),
	FREELANCER("프리랜서"),
	OTHER("기타");

	private final String label;

	Occupation(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
