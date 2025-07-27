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
@Order(3)
public class WebSkinMockDataInitializer implements CommandLineRunner {

    private final ItemRepository itemRepository;
    private final StickerRepository stickerRepository;

    @Value("${cloud.aws.s3.base-url}")
    private String baseUrl;

    @Override
    public void run(String... args) {
        Item webSkin1 = new Item("파란색 웹", ShopCategory.WEB_SKIN, 1000);
        Item webSkin2 = new Item("회색 웹", ShopCategory.WEB_SKIN, 1000);
        Item webSkin3 = new Item("초록색 웹", ShopCategory.WEB_SKIN, 1000);
        Item webSkin4 = new Item("노란색 웹", ShopCategory.WEB_SKIN, 1000);

        itemRepository.saveAll(List.of(webSkin1, webSkin2, webSkin3, webSkin4));

        Sticker sticker1 = new Sticker(
                webSkin1,
                baseUrl + "/web/web_blue.png",
                baseUrl + "/web/web_blue.png"
        );

        Sticker sticker2 = new Sticker(
                webSkin2,
                baseUrl + "/web/web_gray",
                baseUrl + "/web/web_gray.png"
        );
        Sticker sticker3 = new Sticker(
                webSkin3,
                baseUrl + "/web/web_green",
                baseUrl + "/web/web_green.png"
        );Sticker sticker4 = new Sticker(
                webSkin4,
                baseUrl + "/web/web_yellow",
                baseUrl + "/web/web_yellow.png"
        );

        stickerRepository.saveAll(List.of(sticker1, sticker2, sticker3, sticker4));
    }
}

