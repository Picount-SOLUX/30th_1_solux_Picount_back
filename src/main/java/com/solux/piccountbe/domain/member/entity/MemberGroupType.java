package com.solux.piccountbe.domain.member.entity;

import lombok.Getter;

@Getter
public enum MemberGroupType {
    STUDENT_YOUTH("중고등학생"),
    STUDENT_UNIV("대학생"),
    WORKER_2030("2030대 직장인"),
    WORKER_4050("4050대 직장인"),
    FULL_TIME_HOMEMAKER("전업주부"),
    FREELANCER("프리랜서"),
    OTHERS("기타");

    private final String label;

    MemberGroupType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
