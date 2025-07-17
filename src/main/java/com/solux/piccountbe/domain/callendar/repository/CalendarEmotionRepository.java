package com.solux.piccountbe.domain.callendar.repository;

import com.solux.piccountbe.domain.callendar.entity.CalendarEmotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CalendarEmotionRepository extends JpaRepository<CalendarEmotion, Long> {

    boolean existsByMember_MemberIdAndEntryDate(Long memberId, LocalDate entryDate);

    Optional<CalendarEmotion> findByMember_MemberIdAndEntryDate(Long memberId, LocalDate entryDate);
}