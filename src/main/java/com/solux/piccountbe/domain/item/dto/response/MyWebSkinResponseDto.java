package com.solux.piccountbe.domain.item.dto.response;

import com.solux.piccountbe.domain.item.entity.Item;
import com.solux.piccountbe.domain.item.entity.Sticker;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyWebSkinResponseDto {
    private Long itemId;
    private String name;
    private String previewImageUrl;

    public static MyWebSkinResponseDto from(Item item, Sticker sticker){
        return new MyWebSkinResponseDto(
                item.getItemId(),
                item.getName(),
                sticker.getPreviewImageUrl()
        );
    }
}
