package com.solux.piccountbe.domain.member.dto;

import com.solux.piccountbe.domain.member.entity.memberGroupType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProfileResponseDto {
	private Long memberId;
	private String email;
	private String nickname;
	private memberGroupType memberGroupType;
	private String memberGroupTypeLabel;
	private String intro;
	private String friendCode;
	private String profileImageUrl;
}
