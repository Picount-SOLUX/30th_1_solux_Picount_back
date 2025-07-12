package com.solux.piccountbe.domain.item.controller;

import com.solux.piccountbe.config.security.UserDetailsImpl;
import com.solux.piccountbe.domain.item.dto.response.MyCakeDesignResponseDto;
import com.solux.piccountbe.domain.item.dto.response.MyCalendarDesignResponseDto;
import com.solux.piccountbe.domain.item.dto.response.MyPurchaseDto;
import com.solux.piccountbe.domain.item.dto.response.MyWebSkinResponseDto;
import com.solux.piccountbe.domain.item.service.MyItemService;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.global.Response;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
public class MyItemController {
    private final MyItemService myItemService;

    // 내 전체 구매 목록 조회
    @GetMapping("/purchases/me")
    public ResponseEntity<Response<List<MyPurchaseDto>>> getMyPurchases(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (userDetails == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        Member member = userDetails.getMember();
        List<MyPurchaseDto> purchases = myItemService.getMyPurchases(member);
        return ResponseEntity.ok(Response.success("나의 구매 목록 조회 성공", purchases));
    }

    // 내가 보유한 웹스킨 조회
    @GetMapping("/my-web-skins")
    public ResponseEntity<Response<List<MyWebSkinResponseDto>>> getMyWebSkins(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (userDetails == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        Member member = userDetails.getMember();
        List<MyWebSkinResponseDto> mySkins = myItemService.getMyWebSkins(member);
        return ResponseEntity.ok(Response.success("내 웹스킨 목록 조회 성공", mySkins));
    }

    // 내가 보유한 케이크 조회
    @GetMapping("/my-cake-skins")
    public ResponseEntity<Response<List<MyCakeDesignResponseDto>>> getMyCakeDesigns(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (userDetails == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        Member member = userDetails.getMember();
        List<MyCakeDesignResponseDto> result = myItemService.getMyCakeDesigns(member);
        return ResponseEntity.ok(Response.success("내 케이크 디자인 목록 조회 성공", result));
    }

    // 내가 보유한 달력 조회
    @GetMapping("/my-calendar-skins")
    public ResponseEntity<Response<List<MyCalendarDesignResponseDto>>> getMyCalendarDesigns(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (userDetails == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        Member member = userDetails.getMember();
        List<MyCalendarDesignResponseDto> result = myItemService.getMyCalendarDesigns(member);
        return ResponseEntity.ok(Response.success("내 달력 디자인 목록 조회 성공", result));
    }
}
