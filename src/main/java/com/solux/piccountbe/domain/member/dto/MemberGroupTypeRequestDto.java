package com.solux.piccountbe.domain.member.dto;

import com.solux.piccountbe.domain.member.entity.MemberGroupType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberGroupTypeRequestDto {
	private MemberGroupType memberGroupType;
}
