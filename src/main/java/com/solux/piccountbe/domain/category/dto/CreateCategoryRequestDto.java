package com.solux.piccountbe.domain.category.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateCategoryRequestDto {
	List<CategoryRequestDto> categories;
}
