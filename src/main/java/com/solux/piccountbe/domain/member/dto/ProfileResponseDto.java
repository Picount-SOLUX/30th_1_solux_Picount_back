package com.solux.piccountbe.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.solux.piccountbe.domain.member.entity.MemberGroupType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileResponseDto {
	private Long memberId;
	private String email;
	private String nickname;
	private MemberGroupType memberGroupType;
	private String memberGroupTypeLabel;
	private String intro;
	private String friendCode;
	private String profileImageUrl;
}
