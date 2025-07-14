package com.solux.piccountbe.domain.member.entity;

import lombok.Getter;

@Getter
public enum UserGroupType {
	STUDENT_YOUTH("중고등학생"),
	STUDENT_UNIV("대학생"),
	WORKER_2030("2030대 직장인"),
	WORKER_4050("4050대 직장인"),
	YOUNGER("초등학생 이하"),
	OLDER("50대 이상"),
	UNEMPLOYED("무직/비직장인");

	private final String label;

	UserGroupType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
