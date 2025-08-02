package com.solux.piccountbe.domain.budget.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateBudgetRequestDto {

	private LocalDate startDate;
	private LocalDate endDate;
	private Integer totalAmount;
	private List<UpdateBudgetAllocationDto> budgetAllocationList;

}
