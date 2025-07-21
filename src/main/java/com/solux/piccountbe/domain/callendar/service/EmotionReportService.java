package com.solux.piccountbe.domain.callendar.service;

import com.solux.piccountbe.domain.callendar.dto.EmotionReportResponseDto;
import com.solux.piccountbe.domain.callendar.entity.CalendarEmotion;
import com.solux.piccountbe.domain.callendar.entity.EmotionType;
import com.solux.piccountbe.domain.callendar.repository.CalendarEmotionRepository;
import com.solux.piccountbe.domain.callendar.repository.ExpenseDetailRepository;
import com.solux.piccountbe.domain.callendar.repository.IncomeDetailRepository;
import com.solux.piccountbe.domain.member.entity.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmotionReportService {

    private final CalendarEmotionRepository emotionRepository;
    private final IncomeDetailRepository incomeRepository;
    private final ExpenseDetailRepository expenseRepository;

    private static final Set<EmotionType> POSITIVE = Set.of(EmotionType.행복, EmotionType.뿌듯, EmotionType.평온);
    private static final Set<EmotionType> NEGATIVE = Set.of(EmotionType.우울, EmotionType.분노, EmotionType.불안, EmotionType.피곤);

    public EmotionReportResponseDto getEmotionReport(Member member, YearMonth month) {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        // 1. 감정별 일수 카운트
        List<CalendarEmotion> emotionList = emotionRepository.findAllByMemberAndEntryDateBetween(member, start, end);
        Map<String, Integer> emotionCount = new HashMap<>();
        Map<LocalDate, EmotionType> emotionByDate = new HashMap<>();

        for (CalendarEmotion e : emotionList) {
            emotionByDate.put(e.getEntryDate(), e.getEmotion());
            emotionCount.merge(e.getEmotion().name(), 1, Integer::sum);
        }

        // 2. 월 총 수입
        int totalIncome = incomeRepository.findDailyIncomeSums(member, start, end).stream()
                .mapToInt(row -> ((Long) row[1]).intValue())
                .sum();

        // 3. 월 총 지출
        int totalExpense = expenseRepository.findDailyExpenseSums(member, start, end).stream()
                .mapToInt(row -> ((Long) row[1]).intValue())
                .sum();

        // 4. 수입 대비 지출 비율
        double expenseRate = totalIncome == 0 ? 0.0 : ((double) totalExpense / totalIncome) * 100.0;

        // 5. 전월 지출 계산
        YearMonth lastMonth = month.minusMonths(1);
        LocalDate lastStart = lastMonth.atDay(1);
        LocalDate lastEnd = lastMonth.atEndOfMonth();

        int lastMonthExpense = expenseRepository.findDailyExpenseSums(member, lastStart, lastEnd).stream()
                .mapToInt(row -> ((Long) row[1]).intValue())
                .sum();

        int expenseDiff = totalExpense - lastMonthExpense;

        // 6. 감정 날짜 기준으로 긍정/부정 감정 날 지출 합계
        int positiveExpense = 0;
        int negativeExpense = 0;

        Map<LocalDate, Integer> dailyExpenseMap = expenseRepository.findDailyExpenseSums(member, start, end).stream()
                .collect(HashMap::new,
                        (map, row) -> map.put((LocalDate) row[0], ((Long) row[1]).intValue()),
                        HashMap::putAll);

        for (Map.Entry<LocalDate, EmotionType> entry : emotionByDate.entrySet()) {
            int dailyExpense = dailyExpenseMap.getOrDefault(entry.getKey(), 0);
            if (POSITIVE.contains(entry.getValue())) {
                positiveExpense += dailyExpense;
            } else if (NEGATIVE.contains(entry.getValue())) {
                negativeExpense += dailyExpense;
            }
        }

        // 7. 인사이트 문장 생성
        String insight;
        if (positiveExpense > negativeExpense) {
            insight = "긍정적인 날에 소비량이 더 많았습니다.";
        } else if (positiveExpense < negativeExpense) {
            insight = "부정적인 날에 소비량이 더 많았습니다.";
        } else {
            insight = "긍정과 부정 감정 날의 소비량이 같았습니다.";
        }

        return EmotionReportResponseDto.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .expenseRate(Math.round(expenseRate * 10.0) / 10.0) // 소수점 한 자리까지
                .lastMonthExpense(lastMonthExpense)
                .expenseDiffFromLastMonth(expenseDiff)
                .emotionCount(emotionCount)
                .positiveExpense(positiveExpense)
                .negativeExpense(negativeExpense)
                .insight(insight)
                .build();
    }
}
