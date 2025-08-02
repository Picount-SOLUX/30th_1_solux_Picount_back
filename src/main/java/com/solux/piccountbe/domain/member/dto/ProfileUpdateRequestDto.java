package com.solux.piccountbe.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileUpdateRequestDto {
	private String nickname;
	private String intro;
}
