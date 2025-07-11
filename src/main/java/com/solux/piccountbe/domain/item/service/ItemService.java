package com.solux.piccountbe.domain.item.service;

import com.solux.piccountbe.domain.item.dto.ItemResponseDto;
import com.solux.piccountbe.domain.item.entity.Item;
import com.solux.piccountbe.domain.item.entity.ShopCategory;
import com.solux.piccountbe.domain.item.entity.Sticker;
import com.solux.piccountbe.domain.item.repository.ItemRepository;
import com.solux.piccountbe.domain.item.repository.StickerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final StickerRepository stickerRepository;

    public List<ItemResponseDto> getCalendarSkins() {
        List<Item> items = itemRepository.findByCategory(ShopCategory.CALENDER_SKIN);

        return items.stream()
                .map(item -> {
                    String imageUrl = stickerRepository.findByItem(item)
                            .map(Sticker::getStickerUrl)
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

