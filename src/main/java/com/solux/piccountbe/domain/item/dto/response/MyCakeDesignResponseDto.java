package com.solux.piccountbe.domain.item.dto.response;

import com.solux.piccountbe.domain.item.entity.Item;
import com.solux.piccountbe.domain.item.entity.Sticker;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyCakeDesignResponseDto {
    private Long itemId;
    private String name;
    private String previewImageUrl;
    private String skinImageUrl;

    public static MyCakeDesignResponseDto from(Item item, Sticker sticker) {
        return new MyCakeDesignResponseDto(
                item.getItemId(),
                item.getName(),
                sticker.getPreviewImageUrl(),
                sticker.getSkinImageUrl()
        );
    }
}
