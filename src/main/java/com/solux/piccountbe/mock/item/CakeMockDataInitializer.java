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
@Order(2)
public class CakeMockDataInitializer implements CommandLineRunner {

    private final ItemRepository itemRepository;
    private final StickerRepository stickerRepository;

    @Value("${cloud.aws.s3.base-url}")
    private String baseUrl;

    @Override
    public void run(String... args) {
        Item cakeSkin1 = new Item("파란색 케이크", ShopCategory.CAKE_SKIN, 2000);
        Item cakeSkin2 = new Item("체리 케이크", ShopCategory.CAKE_SKIN, 2000);
        Item cakeSkin3 = new Item("초코 케이크", ShopCategory.CAKE_SKIN, 2000);
        Item cakeSkin4 = new Item("딸기 케이크", ShopCategory.CAKE_SKIN, 2000);

        itemRepository.saveAll(List.of(cakeSkin1, cakeSkin2, cakeSkin3, cakeSkin4));

        Sticker sticker1 = new Sticker(
                cakeSkin1,
                baseUrl + "/cake/cake_blue.png",
                baseUrl + "/cake/cake_blue_prev_my.png"
        );

        Sticker sticker2 = new Sticker(
                cakeSkin2,
                baseUrl + "/cake/cake_cherry.png",
                baseUrl + "/cake/cake_cherry_prev_my.png"
        );
        Sticker sticker3 = new Sticker(
                cakeSkin3,
                baseUrl + "/cake/cake_choco.png",
                baseUrl + "/cake/cake_choco_prev_my.png"
        );Sticker sticker4 = new Sticker(
                cakeSkin4,
                baseUrl + "/cake/cake_strawberry.png",
                baseUrl + "/cake/cake_strawberry_prev_my.png"
        );

        stickerRepository.saveAll(List.of(sticker1, sticker2, sticker3, sticker4));
    }
}

