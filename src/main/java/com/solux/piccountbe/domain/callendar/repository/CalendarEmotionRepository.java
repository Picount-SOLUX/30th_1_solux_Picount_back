package com.solux.piccountbe.domain.callendar.repository;

import com.solux.piccountbe.domain.callendar.entity.CalendarEmotion;
import com.solux.piccountbe.domain.member.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CalendarEmotionRepository extends JpaRepository<CalendarEmotion, Long> {

    boolean existsByMember_MemberIdAndEntryDate(Long memberId, LocalDate entryDate);
    List<CalendarEmotion> findAllByMemberAndEntryDateBetween(Member member, LocalDate start, LocalDate end);

    Optional<CalendarEmotion> findByMember_MemberIdAndEntryDate(Long memberId, LocalDate entryDate);
    List<CalendarEmotion> findAllByMember_MemberIdAndEntryDateBetween(Long memberId, LocalDate start, LocalDate end);

}