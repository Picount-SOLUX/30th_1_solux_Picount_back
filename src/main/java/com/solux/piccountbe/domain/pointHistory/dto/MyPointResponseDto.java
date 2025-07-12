package com.solux.piccountbe.domain.pointHistory.dto;

import com.solux.piccountbe.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyPointResponseDto {
    private Long point;

    public static MyPointResponseDto from(Member member) {
        return new MyPointResponseDto(member.getPoint());
    }
}

