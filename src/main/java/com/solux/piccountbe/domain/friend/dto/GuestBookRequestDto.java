package com.solux.piccountbe.domain.friend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GuestBookRequestDto {
    private Long ownerId; // 방명록 주인 ID
    private String content;
}
