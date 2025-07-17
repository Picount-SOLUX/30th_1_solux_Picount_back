package com.solux.piccountbe.domain.callendar.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.solux.piccountbe.domain.member.entity.Member;

@Entity
@Table(name = "calendar_emotion",
        uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "entryDate"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CalendarEmotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDate entryDate;

    @Enumerated(EnumType.STRING)
    private EmotionType emotion;

    private LocalDateTime createdAt;

    public CalendarEmotion(Member member, LocalDate entryDate, EmotionType emotion) {
        this.member = member;
        this.entryDate = entryDate;
        this.emotion = emotion;
        this.createdAt = LocalDateTime.now();
    }

    public static CalendarEmotion of(Member member, LocalDate entryDate, EmotionType emotion) {
        return new CalendarEmotion(member, entryDate, emotion);
    }

    // 감정 수정
    public void updateEmotion(EmotionType newEmotion) {
        this.emotion = newEmotion;
        this.createdAt = LocalDateTime.now();
    }
}
