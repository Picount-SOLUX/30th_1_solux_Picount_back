package com.solux.piccountbe.domain.item.dto.response;

import com.solux.piccountbe.domain.item.entity.Item;
import com.solux.piccountbe.domain.item.entity.Purchase;
import com.solux.piccountbe.domain.item.entity.ShopCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyPurchaseDto {
    private Long itemId;
    private String name;
    private ShopCategory category;
    private LocalDateTime purchasedAt;
    private String previewImageUrl;

    public static MyPurchaseDto from(Purchase purchase, String previewImageUrl) {
        Item item = purchase.getItem();
        return new MyPurchaseDto(
                item.getItemId(),
                item.getName(),
                item.getCategory(),
                purchase.getPurchasedAt(),
                previewImageUrl
        );
    }
}
