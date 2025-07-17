package com.solux.piccountbe.domain.callendar.dto;

import com.solux.piccountbe.domain.callendar.entity.EmotionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CalendarMonthlySummaryResponseDto {
    private String month; // YYYY-MM
    private List<DailySummary> summary;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class DailySummary {
        private LocalDate date;
        private EmotionType emotion; // enum 그대로 넘기기
        private int incomeTotal;
        private int expenseTotal;
    }
}
