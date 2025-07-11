package com.solux.piccountbe.domain.item.service;

import com.solux.piccountbe.domain.item.dto.response.MyCakeDesignResponseDto;
import com.solux.piccountbe.domain.item.dto.response.MyCalendarDesignResponseDto;
import com.solux.piccountbe.domain.item.dto.response.MyWebSkinResponseDto;
import com.solux.piccountbe.domain.item.entity.Purchase;
import com.solux.piccountbe.domain.item.entity.ShopCategory;
import com.solux.piccountbe.domain.item.entity.Sticker;
import com.solux.piccountbe.domain.item.repository.PurchaseRepository;
import com.solux.piccountbe.domain.item.repository.StickerRepository;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyItemService {
    private final PurchaseRepository purchaseRepository;
    private final StickerRepository stickerRepository;

    // 내가 보유한 웹스킨 조회
    public List<MyWebSkinResponseDto> getMyWebSkins(Member member) {
        List<Purchase> purchases = purchaseRepository.findByMember(member);

        return purchases.stream()
                .map(Purchase::getItem)
                .filter(item -> item.getCategory() == ShopCategory.WEB_SKIN)
                .map(item -> {
                    Sticker sticker = stickerRepository.findByItem(item)
                            .orElseThrow(() -> new CustomException(ErrorCode.STICKER_NOT_FOUND));
                    return MyWebSkinResponseDto.from(item, sticker);
                })
                .toList();
    }

    // 내가 보유한 케이크 조회
    @Transactional
    public List<MyCakeDesignResponseDto> getMyCakeDesigns(Member member) {
        List<Purchase> purchases = purchaseRepository.findByMember(member);

        return purchases.stream()
                .map(Purchase::getItem)
                .filter(item -> item.getCategory() == ShopCategory.CAKE_SKIN)
                .map(item -> {
                    Sticker sticker = stickerRepository.findByItem(item)
                            .orElseThrow(() -> new CustomException(ErrorCode.STICKER_NOT_FOUND));
                    return MyCakeDesignResponseDto.from(item, sticker);
                })
                .toList();
    }

    // 내가 보유한 달력 조회
    @Transactional
    public List<MyCalendarDesignResponseDto> getMyCalendarDesigns(Member member) {
        List<Purchase> purchases = purchaseRepository.findByMember(member);

        return purchases.stream()
                .map(Purchase::getItem)
                .filter(item -> item.getCategory() == ShopCategory.CALENDAR_SKIN)
                .map(item -> {
                    Sticker sticker = stickerRepository.findByItem(item)
                            .orElseThrow(() -> new CustomException(ErrorCode.STICKER_NOT_FOUND));
                    return MyCalendarDesignResponseDto.from(item, sticker);
                })
                .toList();
    }

}
