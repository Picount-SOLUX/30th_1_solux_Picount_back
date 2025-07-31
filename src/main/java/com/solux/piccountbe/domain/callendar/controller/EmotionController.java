package com.solux.piccountbe.domain.callendar.controller;

import com.solux.piccountbe.domain.callendar.dto.EmotionRequestDto;
import com.solux.piccountbe.domain.callendar.service.EmotionService;
import com.solux.piccountbe.config.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/calendar/emotion")
@RequiredArgsConstructor
public class EmotionController {

    private final EmotionService emotionService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> registerEmotion(
            @RequestBody @Valid EmotionRequestDto request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        emotionService.registerEmotion(userDetails.getMember(), request);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "감정 스티커 등록 성공");
        response.put("data", null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteEmotion(
            @RequestParam("date") String dateStr,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        emotionService.deleteEmotion(userDetails.getMember(), dateStr);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "감정 스티커 삭제 성공");
        response.put("data", null);
        return ResponseEntity.ok(response);
    }
}