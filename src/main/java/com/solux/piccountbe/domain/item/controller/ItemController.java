package com.solux.piccountbe.domain.item.controller;

import com.solux.piccountbe.config.security.UserDetailsImpl;
import com.solux.piccountbe.domain.item.dto.request.PurchaseRequestDto;
import com.solux.piccountbe.domain.item.dto.response.ItemResponseDto;
import com.solux.piccountbe.domain.item.dto.response.PurchaseResponseDto;
import com.solux.piccountbe.domain.item.service.ItemService;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.global.Response;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemController {
    private final ItemService itemService;

    // 상품 구매
    @PostMapping("/purchases")
    public ResponseEntity<Response<PurchaseResponseDto>> purchaseItem(
            @RequestBody PurchaseRequestDto request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (userDetails == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        Long memberId = userDetails.getMember().getMemberId();
        PurchaseResponseDto response = itemService.purchaseItem(request.getItemId(), memberId);
        return ResponseEntity.ok(Response.success("상품 구매 성공", response));
    }

    // 달력 꾸미기 상품 조회
    @GetMapping("/calendar-skins")
    public ResponseEntity<Response<List<ItemResponseDto>>> getCalendarSkins(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (userDetails == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        List<ItemResponseDto> calendarSkins = itemService.getCalendarSkins();
        return ResponseEntity.ok(Response.success("달력 꾸미기 상품 목록 조회 성공", calendarSkins));
    }

    // 케이크 꾸미기 상품 조회
    @GetMapping("/cake-skins")
    public ResponseEntity<Response<List<ItemResponseDto>>> getCakeSkins(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (userDetails == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        List<ItemResponseDto> cakeSkins = itemService.getCakeSkins();
        return ResponseEntity.ok(Response.success("케이크 꾸미기 상품 목록 조회 성공", cakeSkins));
    }

    // 웹스킨 꾸미기 상품 조회
    @GetMapping("/web-skins")
    public ResponseEntity<Response<List<ItemResponseDto>>> getWebSkins(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (userDetails == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        List<ItemResponseDto> webSkins = itemService.getWebSkins();
        return ResponseEntity.ok(Response.success("웹 스킨 꾸미기 상품 목록 조회 성공", webSkins));
    }

}
