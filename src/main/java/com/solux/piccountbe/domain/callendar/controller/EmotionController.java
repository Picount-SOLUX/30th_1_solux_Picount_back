package com.solux.piccountbe.domain.callendar.controller;

import com.solux.piccountbe.domain.callendar.dto.EmotionRequestDto;
import com.solux.piccountbe.domain.callendar.service.EmotionService;
import com.solux.piccountbe.config.security.UserDetailsImpl;
import com.solux.piccountbe.global.Response;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/calendar/emotion")
@RequiredArgsConstructor
public class EmotionController {

    private final EmotionService emotionService;

    @PostMapping
    public ResponseEntity<Response<Void>> registerEmotion(
            @RequestBody @Valid EmotionRequestDto request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        emotionService.registerEmotion(userDetails.getMember(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.success("감정 스티커 등록 성공", null));
    }
}