package com.solux.piccountbe.domain.callendar.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.solux.piccountbe.domain.callendar.dto.CalendarRecordRequestDto;
import com.solux.piccountbe.domain.callendar.service.CalendarService;
import com.solux.piccountbe.config.security.UserDetailsImpl;
import com.solux.piccountbe.global.Response;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    // 달력 등록
    @PostMapping("/record")
    public ResponseEntity<Response<Void>> createCalendarEntry(
            @RequestPart("request") CalendarRecordRequestDto request,
            @RequestPart(value = "photos", required = false) MultipartFile[] photos,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        calendarService.createEntry(request, photos, userDetails.getMember());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.success("가계부 등록 성공", null));
    }
}