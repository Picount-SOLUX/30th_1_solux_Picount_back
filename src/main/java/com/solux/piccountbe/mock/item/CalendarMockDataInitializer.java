package com.solux.piccountbe.mock.item;

import com.solux.piccountbe.domain.item.entity.Item;
import com.solux.piccountbe.domain.item.entity.ShopCategory;
import com.solux.piccountbe.domain.item.entity.Sticker;
import com.solux.piccountbe.domain.item.repository.ItemRepository;
import com.solux.piccountbe.domain.item.repository.StickerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${cloud.aws.s3.base-url}")
    private String baseUrl;

    @Override
    public void run(String... args) {
        Item calendarSkin1 = new Item("천사 달력", ShopCategory.CALENDAR_SKIN, 3000);
        Item calendarSkin2 = new Item("창 달력", ShopCategory.CALENDAR_SKIN, 2500);
        Item calendarSkin3 = new Item("왕관 달력", ShopCategory.CALENDAR_SKIN, 2500);
        Item calendarSkin4 = new Item("토마토 달력", ShopCategory.CALENDAR_SKIN, 2500);

        itemRepository.saveAll(List.of(calendarSkin1, calendarSkin2, calendarSkin3, calendarSkin4));

        Sticker sticker1 = new Sticker(
                calendarSkin1,
                baseUrl + "/calendar/cal_angel_skin.png",
                baseUrl + "/calendar/cal_angel_prev.png"
        );

        Sticker sticker2 = new Sticker(
                calendarSkin2,
                baseUrl + "/calendar/cal_chang_skin",
                baseUrl + "/calendar/cal_chang_prev.png"
        );
        Sticker sticker3 = new Sticker(
                calendarSkin3,
                baseUrl + "/calendar/cal_tiara_skin",
                baseUrl + "/calendar/cal_tiara_prev.png"
        );Sticker sticker4 = new Sticker(
                calendarSkin4,
                baseUrl + "/calendar/cal_tomato_skin",
                baseUrl + "/calendar/cal_tomato_prev.png"
        );

        stickerRepository.saveAll(List.of(sticker1, sticker2, sticker3, sticker4));
    }
}

