package com.solux.piccountbe.domain.callendar.repository;

import java.time.LocalDate;
import java.util.List;

import com.solux.piccountbe.domain.callendar.entity.CalendarEntry;
import com.solux.piccountbe.domain.callendar.entity.ExpenseDetail;
import com.solux.piccountbe.domain.member.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExpenseDetailRepository extends JpaRepository<ExpenseDetail, Long> {

    // 요약 조회용: 한 달 동안 일자별 총 지출 합계
    @Query("""
        SELECT e.entryDate, SUM(x.amount)
        FROM ExpenseDetail x
        JOIN x.calendarEntry e
        WHERE e.member = :member AND e.entryDate BETWEEN :start AND :end
        GROUP BY e.entryDate
    """)
    List<Object[]> findDailyExpenseSums(@Param("member") Member member,
                                        @Param("start") LocalDate start,
                                        @Param("end") LocalDate end);

    // 상세 조회용: 특정 CalendarEntry의 지출 목록
    List<ExpenseDetail> findAllByCalendarEntry(CalendarEntry calendarEntry);
}

//List<ExpenseDetail> findAllByCalendarEntry(CalendarEntry calendarEntry);