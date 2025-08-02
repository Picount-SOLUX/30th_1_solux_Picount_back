package com.solux.piccountbe.domain.callendar.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.solux.piccountbe.config.security.UserDetailsImpl;
import com.solux.piccountbe.domain.callendar.dto.*;
import com.solux.piccountbe.domain.callendar.service.CalendarService;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.global.Response;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    // 달력 등록
    @PostMapping(value = "/record", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, Object>> createCalendarEntry(
            @RequestPart("request") CalendarRecordRequestDto request,
            @RequestPart(value = "photos", required = false) MultipartFile[] photos,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        calendarService.createEntry(request, photos, userDetails.getMember());
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "가계부 등록 성공");
        response.put("data", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 상세 조회
    @GetMapping("/record")
    public ResponseEntity<Map<String, Object>> getCalendarRecordDetail(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Member loginMember = userDetails.getMember();
        CalendarRecordDetailResponseDto responseDto = calendarService.getCalendarDetail(loginMember, date);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "달력 상세 조회 성공");
        response.put("data", responseDto);
        return ResponseEntity.ok(response);
    }

    // 요약 조회
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Member loginMember = userDetails.getMember();
        CalendarMonthlySummaryResponseDto responseDto = calendarService.getMonthlySummary(loginMember, year, month);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "달력 요약 조회 성공");
        response.put("data", responseDto);
        return ResponseEntity.ok(response);
    }

    // 수정하기
    @PatchMapping(value = "/record", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, Object>> updateCalendarEntry(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestPart("request") CalendarRecordUpdateRequestDto request,
            @RequestPart(value = "photos", required = false) MultipartFile[] photos,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        calendarService.updateEntry(request, photos, userDetails.getMember(), date);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "가계부 수정 성공");
        response.put("data", null);
        return ResponseEntity.ok(response);
    }

}