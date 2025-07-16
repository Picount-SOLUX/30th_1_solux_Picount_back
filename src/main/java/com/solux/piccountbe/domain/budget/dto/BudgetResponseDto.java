package com.solux.piccountbe.domain.budget.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BudgetResponseDto {
	private long budgetId;
	private LocalDate startDate;
	private LocalDate endDate;
	private int totalAmount;
	private boolean isActive;
}
