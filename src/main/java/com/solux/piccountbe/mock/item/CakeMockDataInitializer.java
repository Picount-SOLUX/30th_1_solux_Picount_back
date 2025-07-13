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
@Order(2)
public class CakeMockDataInitializer implements CommandLineRunner {

    private final ItemRepository itemRepository;
    private final StickerRepository stickerRepository;

    @Override
    public void run(String... args) {
        Item cakeSkin1 = new Item("핑크 케이크", ShopCategory.CAKE_SKIN, 3000);
        Item cakeSkin2 = new Item("생크림 케이크", ShopCategory.CAKE_SKIN, 2500);

        itemRepository.saveAll(List.of(cakeSkin1, cakeSkin2));

        Sticker sticker1 = new Sticker(cakeSkin1, "pink_cake_skin.png", "pink_cake_preview.png");
        Sticker sticker2 = new Sticker(cakeSkin2, "cream_cake_skin.png", "cream_cake_preview.png");

        stickerRepository.saveAll(List.of(sticker1, sticker2));
    }

}

