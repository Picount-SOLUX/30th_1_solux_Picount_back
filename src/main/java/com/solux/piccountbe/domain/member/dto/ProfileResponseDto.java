package com.solux.piccountbe.domain.member.dto;

import com.solux.piccountbe.domain.member.entity.UserGroupType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProfileResponseDto {
	private Long memberId;
	private String email;
	private String nickname;
	private UserGroupType userGroupType;
	private String userGroupTypeLabel;
	private String intro;
	private String friendCode;
	private String profileImageUrl;
}
