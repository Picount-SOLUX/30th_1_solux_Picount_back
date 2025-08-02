package com.solux.piccountbe.domain.callendar.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.callendar.entity.CalendarEntry;
import com.solux.piccountbe.domain.member.entity.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CalendarEntryRepository extends JpaRepository<CalendarEntry, Long> {
    Optional<CalendarEntry> findByMemberAndEntryDate(Member member, LocalDate entryDate);

    boolean existsByMemberAndEntryDate(Member member, LocalDate entryDate);

    @Query("SELECT COUNT(c) FROM CalendarEntry c " +
            "WHERE c.member = :member AND c.memo IS NOT NULL AND c.entryDate >= :baseDate")
    int countNoSpendingDaysFromDate(@Param("member") Member member, @Param("baseDate") LocalDate baseDate);

    // 출석 보상 수령 완료된 날짜만 필터
    @Query("SELECT c.entryDate FROM CalendarEntry c " +
            "WHERE c.member = :member AND c.point > 0 AND c.entryDate IN :targetDates")
    List<LocalDate> findAttendanceRewardDates(@Param("member") Member member, @Param("targetDates") List<LocalDate> targetDates);

}