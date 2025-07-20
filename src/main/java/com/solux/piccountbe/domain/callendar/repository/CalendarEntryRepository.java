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

    @Query("SELECT c.entryDate FROM CalendarEntry c WHERE c.member = :member AND c.entryDate IN :dates AND c.point IS NOT NULL")
    List<LocalDate> findAttendedDatesByMemberAndDates(@Param("member") Member member, @Param("dates") List<LocalDate> dates);

    @Query("SELECT COUNT(c) FROM CalendarEntry c WHERE c.member = :member AND c.memo IS NOT NULL")
    int countNoSpendingDays(@Param("member") Member member);

    boolean existsByMemberAndEntryDate(Member member, LocalDate entryDate);
}