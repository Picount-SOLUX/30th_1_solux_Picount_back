package com.solux.piccountbe.domain.callendar.controller;

import com.solux.piccountbe.config.security.UserDetailsImpl;
import com.solux.piccountbe.domain.callendar.dto.EmotionReportResponseDto;
import com.solux.piccountbe.domain.callendar.service.EmotionReportService;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/calendar/report")
@RequiredArgsConstructor
public class EmotionReportController {

    private final EmotionReportService emotionReportService;

    @GetMapping("/emotion")
    public ResponseEntity<Map<String, Object>> getEmotionReport(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam Long ownerId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Member loginMember = userDetails.getMember();

        if (!loginMember.getMemberId().equals(ownerId)) {
            throw new CustomException(ErrorCode.CALENDAR_FORBIDDEN);
        }

        YearMonth targetMonth = YearMonth.of(year, month);
        EmotionReportResponseDto report = emotionReportService.getEmotionReport(loginMember, targetMonth);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "감정 리포트 조회 성공");
        response.put("data", report);

        return ResponseEntity.ok(response);
    }
}
