package com.solux.piccountbe.domain.item.service;

import com.solux.piccountbe.domain.item.dto.ItemResponseDto;
import com.solux.piccountbe.domain.item.entity.Item;
import com.solux.piccountbe.domain.item.entity.ShopCategory;
import com.solux.piccountbe.domain.item.entity.Sticker;
import com.solux.piccountbe.domain.item.repository.ItemRepository;
import com.solux.piccountbe.domain.item.repository.StickerRepository;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final StickerRepository stickerRepository;

    // 달력 꾸미기 상품 조회
    public List<ItemResponseDto> getCalendarSkins() {
        List<Item> items = itemRepository.findByCategory(ShopCategory.CALENDAR_SKIN);

        if (items.isEmpty()) {
            throw new CustomException(ErrorCode.NO_ITEMS_FOUND);
        }

        return items.stream()
                .map(item -> {
                    String imageUrl = stickerRepository.findByItem(item)
                            .map(Sticker::getSkinImageUrl)
                            .orElse(null);
                    return new ItemResponseDto(
                            item.getItemId(),
                            item.getName(),
                            item.getCategory(),
                            item.getPrice(),
                            imageUrl
                    );
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
                            .map(Sticker::getSkinImageUrl)
                            .orElse(null);
                    return new ItemResponseDto(
                            item.getItemId(),
                            item.getName(),
                            item.getCategory(),
                            item.getPrice(),
                            imageUrl
                    );
                })
                .toList();
    }


}

