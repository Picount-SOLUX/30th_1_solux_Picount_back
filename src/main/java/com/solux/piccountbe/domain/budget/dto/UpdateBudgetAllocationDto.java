package com.solux.piccountbe.domain.budget.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateBudgetAllocationDto {

	private Long categoryId;
	private int amount;

}
