package com.solux.piccountbe.domain.budget.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateBudgetRequestDto {
	private LocalDate startDate;
	private LocalDate endDate;
	private int totalAmount;
}
