package com.solux.piccountbe.domain.item.dto.response;

import com.solux.piccountbe.domain.item.entity.Item;
import com.solux.piccountbe.domain.item.entity.ShopCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemResponseDto {
    private Long itemId;
    private String name;
    private ShopCategory category;
    private Integer price;
    private String imageUrl;

    public static ItemResponseDto from(Item item, String imageUrl) {
        return new ItemResponseDto(
                item.getItemId(),
                item.getName(),
                item.getCategory(),
                item.getPrice(),
                imageUrl
        );
    }
}
