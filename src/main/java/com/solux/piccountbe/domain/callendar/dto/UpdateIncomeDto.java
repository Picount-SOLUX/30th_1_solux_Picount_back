package com.solux.piccountbe.domain.callendar.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateIncomeDto {
    private Long incomeId;
    private Long categoryId;
    private Integer amount;
    private boolean delete;
}