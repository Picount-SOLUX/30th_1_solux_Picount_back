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
@Order(3)
public class WebSkinMockDataInitializer implements CommandLineRunner {

    private final ItemRepository itemRepository;
    private final StickerRepository stickerRepository;

    @Override
    public void run(String... args) {
        if (!itemRepository.findByCategory(ShopCategory.WEB_SKIN).isEmpty()) return;

        Item webSkin1 = new Item("분홍색 스킨", ShopCategory.WEB_SKIN, 1000);
        Item webSkin2 = new Item("파란색 스킨", ShopCategory.WEB_SKIN, 1200);

        itemRepository.saveAll(List.of(webSkin1, webSkin2));

        Sticker s1 = new Sticker(webSkin1, null, "pink_preview.png");
        Sticker s2 = new Sticker(webSkin2, null , "blue_preview.png");

        stickerRepository.saveAll(List.of(s1, s2));
    }
}

