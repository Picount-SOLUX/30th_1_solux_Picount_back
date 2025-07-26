package com.solux.piccountbe.domain.category.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AllCategoryResponseDto {
	List<CategoryResponseDto> categories;
}
