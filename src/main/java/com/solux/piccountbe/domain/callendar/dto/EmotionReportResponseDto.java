package com.solux.piccountbe.domain.callendar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class EmotionReportResponseDto {

    private int totalIncome;                         // 해당 월 총 수입
    private int totalExpense;                        // 해당 월 총 지출
    private double expenseRate;                      // 수입 대비 지출 비율 (%)

    private int lastMonthExpense;                    // 전월 총 지출
    private int expenseDiffFromLastMonth;            // 전월 대비 지출 증감액

    private Map<String, Integer> emotionCount;       // 감정별 일수 카운트

    private int positiveExpense;                     // 긍정 감정 날 지출 총합
    private int negativeExpense;                     // 부정 감정 날 지출 총합

    private String insight;                          // 인사이트 문장
}
