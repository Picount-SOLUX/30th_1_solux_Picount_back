package com.solux.piccountbe.mock.item;

import com.solux.piccountbe.domain.item.entity.Item;
import com.solux.piccountbe.domain.item.entity.ShopCategory;
import com.solux.piccountbe.domain.item.entity.Sticker;
import com.solux.piccountbe.domain.item.repository.ItemRepository;
import com.solux.piccountbe.domain.item.repository.StickerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Order(1)
public class CalendarMockDataInitializer implements CommandLineRunner {

    private final ItemRepository itemRepository;
    private final StickerRepository stickerRepository;

    @Override
    public void run(String... args) {
        if (itemRepository.count() == 0) {
            // 달력 꾸미기 아이템
            Item calendarSkin1 = new Item("천사 스킨", ShopCategory.CALENDAR_SKIN, 2000);
            Item calendarSkin2 = new Item("창 스킨", ShopCategory.CALENDAR_SKIN, 1500);

            itemRepository.saveAll(List.of(calendarSkin1, calendarSkin2));

            Sticker sticker1 = new Sticker(calendarSkin1, "angel_skin_skin.png","angel_skin_preview.png");
            Sticker sticker2 = new Sticker(calendarSkin2, "chang_skin_skin.png","chang_skin_preview.png");

            stickerRepository.saveAll(List.of(sticker1, sticker2));

            System.out.println("Mock ) 달력 스킨 아이템 2개 등록 완료");
        }
    }
}

