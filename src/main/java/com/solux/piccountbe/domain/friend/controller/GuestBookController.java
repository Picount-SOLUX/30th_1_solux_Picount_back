package com.solux.piccountbe.domain.friend.controller;

import com.solux.piccountbe.domain.friend.dto.GuestBookRequestDto;
import com.solux.piccountbe.domain.friend.dto.GuestBookSummaryDto;
import com.solux.piccountbe.domain.friend.service.GuestBookService;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.config.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GuestBookController {

    private final GuestBookService guestBookService;

    // 방명록 작성
    @PostMapping("/api/guestbook")
    public ResponseEntity<String> createGuestbook(
            @RequestBody GuestBookRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Member writer = userDetails.getMember(); // 로그인한 유저
        guestBookService.createGuestbook(writer, requestDto);
        return ResponseEntity.ok("방명록 작성 완료");
    }

    // 방명록 요약 조회
    @GetMapping("/api/guestbook/summary")
    public ResponseEntity<Map<String, Object>> getGuestBookSummary(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PageableDefault(size = 3, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Member loginMember = userDetails.getMember();
        Page<GuestBookSummaryDto> pageResult = guestBookService.getGuestBooks(loginMember, pageable);

        Map<String, Object> data = new HashMap<>();
        data.put("content", pageResult.getContent());
        data.put("page", pageResult.getNumber());
        data.put("size", pageResult.getSize());
        data.put("totalElements", pageResult.getTotalElements());
        data.put("totalPages", pageResult.getTotalPages());
        data.put("hasNext", pageResult.hasNext());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "방명록 요약 조회 성공");
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    // 방명록 상세 조회

}
