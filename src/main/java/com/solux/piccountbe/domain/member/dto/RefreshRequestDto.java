package com.solux.piccountbe.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshRequestDto {
	private String refreshToken;
}
