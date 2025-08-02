package com.solux.piccountbe.domain.budget.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BudgetAllocationDto {
	private Long budgetAllocationId;
	private Long categoryId;
	private String CategoryName;
	private int amount;
}
