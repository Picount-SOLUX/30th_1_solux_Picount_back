package com.solux.piccountbe.domain.callendar.service;

import com.solux.piccountbe.domain.callendar.entity.CalendarEmotion;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.callendar.dto.EmotionRequestDto;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;
import com.solux.piccountbe.domain.callendar.repository.CalendarEmotionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EmotionService {

    private final CalendarEmotionRepository emotionRepository;

    public void registerEmotion(Member member, EmotionRequestDto request) {
        LocalDate date = LocalDate.parse(request.getEntryDate());

        // 이미 감정이 등록된 날인지 체크
        if (emotionRepository.existsByMember_MemberIdAndEntryDate(member.getMemberId(), date)) {
            throw new CustomException(ErrorCode.CALENDAR_EMOTION_ALREADY_EXISTS);
        }

        // 감정 스티커 생성 및 저장
        CalendarEmotion emotion = CalendarEmotion.of(member, date, request.getEmotion());
        emotionRepository.save(emotion);
    }
}
