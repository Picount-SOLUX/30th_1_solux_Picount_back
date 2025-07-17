package com.solux.piccountbe.domain.callendar.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CalendarRecordRequestDto {
    private LocalDate entryDate;         // 비우면 오늘로 처리
    private String memo;
    private List<IncomeDto> incomeList;
    private List<ExpenseDto> expenseList;
}
