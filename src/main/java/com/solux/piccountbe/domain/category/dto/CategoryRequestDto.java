package com.solux.piccountbe.domain.category.dto;

import com.solux.piccountbe.domain.category.entity.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CategoryRequestDto {
	private String categoryName;
	private Type type;
}
