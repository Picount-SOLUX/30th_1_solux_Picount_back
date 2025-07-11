package com.solux.piccountbe.domain.item.dto;

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
}
