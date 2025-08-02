package com.solux.piccountbe.domain.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GuestBookSummaryDto {
    private Long guestbookId;
    private String writerProfileImage;
    private String content;
    private LocalDateTime createdAt;
}
