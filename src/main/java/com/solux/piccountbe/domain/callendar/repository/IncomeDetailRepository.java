package com.solux.piccountbe.domain.callendar.repository;

import java.time.LocalDate;
import java.util.List;

import com.solux.piccountbe.domain.callendar.entity.IncomeDetail;
import com.solux.piccountbe.domain.member.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IncomeDetailRepository extends JpaRepository<IncomeDetail, Long> {

    @Query("""
        SELECT e.entryDate, SUM(i.amount)
        FROM IncomeDetail i
        JOIN i.calendarEntry e
        WHERE e.member = :member AND e.entryDate BETWEEN :start AND :end
        GROUP BY e.entryDate
    """)
    List<Object[]> findDailyIncomeSums(@Param("member") Member member,
                                       @Param("start") LocalDate start,
                                       @Param("end") LocalDate end);

    // 상세 조회용 - 특정 날짜 수입 목록
    List<IncomeDetail> findAllByCalendarEntry(com.solux.piccountbe.domain.callendar.entity.CalendarEntry calendarEntry);
}

//List<IncomeDetail> findAllByCalendarEntry(CalendarEntry calendarEntry);
