package com.solux.piccountbe.domain.callendar.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CalendarRecordUpdateRequestDto {
    private String memo;
    private List<UpdateIncomeDto> incomeList;
    private List<UpdateExpenseDto> expenseList;
}
