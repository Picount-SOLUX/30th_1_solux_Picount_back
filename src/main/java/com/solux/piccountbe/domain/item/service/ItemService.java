package com.solux.piccountbe.domain.item.service;

import com.solux.piccountbe.domain.item.dto.response.ItemResponseDto;
import com.solux.piccountbe.domain.item.dto.response.PurchaseResponseDto;
import com.solux.piccountbe.domain.item.entity.Item;
import com.solux.piccountbe.domain.item.entity.Purchase;
import com.solux.piccountbe.domain.item.entity.ShopCategory;
import com.solux.piccountbe.domain.item.entity.Sticker;
import com.solux.piccountbe.domain.item.repository.ItemRepository;
import com.solux.piccountbe.domain.item.repository.PurchaseRepository;
import com.solux.piccountbe.domain.item.repository.StickerRepository;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.member.service.MemberService;
import com.solux.piccountbe.domain.pointHistory.entity.Reason;
import com.solux.piccountbe.domain.pointHistory.service.PointService;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final StickerRepository stickerRepository;
    private final PurchaseRepository purchaseRepository;
    private final PointService pointService;
    private final MemberService memberService;

    // 상품 구매
    @Transactional
    public PurchaseResponseDto purchaseItem(Long itemId, Long memberId) {
        // 영속 상태 member로 조회, 멤버 조회는 MemberService
        Member member = memberService.getMemberById(memberId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));

        // 포인트 차감 및 기록은 PointService
        pointService.deductPoints(member, item.getPrice().longValue(), Reason.ITEM_PURCHASE);

        Purchase purchase = new Purchase(member, item, LocalDateTime.now());
        purchaseRepository.save(purchase);

        return new PurchaseResponseDto(
                purchase.getPurchaseId(),
                item.getItemId(),
                purchase.getPurchasedAt()
        );
    }

    // 달력 꾸미기 상품 조회
    public List<ItemResponseDto> getCalendarSkins() {
        List<Item> items = itemRepository.findByCategory(ShopCategory.CALENDAR_SKIN);

        if (items.isEmpty()) {
            throw new CustomException(ErrorCode.NO_ITEMS_FOUND);
        }

        return items.stream()
                .map(item -> {
                    String imageUrl = stickerRepository.findByItem(item)
                            .map(Sticker::getPreviewImageUrl)
                            .orElse(null);
                    return ItemResponseDto.from(item, imageUrl);
                })
                .toList();
    }

    // 케이크 꾸미기 상품 조회
    public List<ItemResponseDto> getCakeSkins() {
        List<Item> items = itemRepository.findByCategory(ShopCategory.CAKE_SKIN);

        if (items.isEmpty()) {
            throw new CustomException(ErrorCode.NO_ITEMS_FOUND);
        }

        return items.stream()
                .map(item -> {
                    String imageUrl = stickerRepository.findByItem(item)
                            .map(Sticker::getPreviewImageUrl)
                            .orElse(null);
                    return ItemResponseDto.from(item, imageUrl);
                })
                .toList();
    }

    // 웹 스킨 꾸미기 상품 조회
    public List<ItemResponseDto> getWebSkins() {
        List<Item> items = itemRepository.findByCategory(ShopCategory.WEB_SKIN);

        if (items.isEmpty()) {
            throw new CustomException(ErrorCode.NO_ITEMS_FOUND);
        }

        return items.stream()
                .map(item -> {
                    String imageUrl = stickerRepository.findByItem(item)
                            .map(Sticker::getPreviewImageUrl)  // 기존 stickerUrl 또는 skinImageUrl
                            .orElse(null);
                    return ItemResponseDto.from(item, imageUrl);
                })
                .toList();
    }


}

