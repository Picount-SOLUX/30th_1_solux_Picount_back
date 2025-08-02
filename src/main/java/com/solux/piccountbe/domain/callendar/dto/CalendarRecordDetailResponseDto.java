package com.solux.piccountbe.domain.callendar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CalendarRecordDetailResponseDto {
    private LocalDate date;
    private String memo;
    private List<IncomeDto> incomes;
    private List<ExpenseDto> expenses;
    private List<String> imageUrls;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class IncomeDto {
        private Long categoryId;
        private String categoryName;
        private Integer amount;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ExpenseDto {
        private Long categoryId;
        private String categoryName;
        private Integer amount;
    }
}
