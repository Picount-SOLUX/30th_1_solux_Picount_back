package com.solux.piccountbe.domain.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GuestbookMyResponseDto {
    private Long guestbookId;
    private String ownerNickname;
    private String content;
    private LocalDateTime createdAt;
}
