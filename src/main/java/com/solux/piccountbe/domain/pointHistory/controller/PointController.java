package com.solux.piccountbe.domain.pointHistory.controller;

import com.solux.piccountbe.config.security.UserDetailsImpl;
import com.solux.piccountbe.domain.pointHistory.dto.MyPointResponseDto;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.pointHistory.service.PointService;
import com.solux.piccountbe.global.Response;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/points")
public class PointController {

    private final PointService pointService;

    @GetMapping("/my")
    public ResponseEntity<Response<MyPointResponseDto>> getMyPoint(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (userDetails == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        Member member = userDetails.getMember();
        MyPointResponseDto response = pointService.getMyPoint(member);
        return ResponseEntity.ok(Response.success("내 포인트 조회 성공", response));
    }
}

