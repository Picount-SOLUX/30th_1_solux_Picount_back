package com.solux.piccountbe.domain.category.dto;

import com.solux.piccountbe.domain.category.entity.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetCategoryResponseDto {
	private Long categoryId;
	private String categoryName;
	private Type type;
	private String typeLabel;
}
