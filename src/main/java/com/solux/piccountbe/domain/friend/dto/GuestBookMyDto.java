package com.solux.piccountbe.domain.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GuestBookMyDto {
    private Long guestbookId;
    private String friendName;
    private LocalDateTime createdAt;
    private String content;
}
