package com.solux.piccountbe.domain.callendar.service;

import com.solux.piccountbe.domain.callendar.entity.CalendarEmotion;
import com.solux.piccountbe.domain.callendar.entity.EmotionType;
import com.solux.piccountbe.domain.callendar.dto.EmotionRequestDto;
import com.solux.piccountbe.domain.callendar.repository.CalendarEmotionRepository;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EmotionService {

    private final CalendarEmotionRepository emotionRepository;

    @Transactional
    public void registerEmotion(Member member, EmotionRequestDto request) {
        LocalDate date = LocalDate.parse(request.getEntryDate());

        // 기존 감정이 있으면 수정, 없으면 새로 저장
        CalendarEmotion existing = emotionRepository
                .findByMember_MemberIdAndEntryDate(member.getMemberId(), date)
                .orElse(null);

        if (existing != null) {
            existing.updateEmotion(request.getEmotion());
        } else {
            CalendarEmotion emotion = CalendarEmotion.of(member, date, request.getEmotion());
            emotionRepository.save(emotion);
        }
    }
}