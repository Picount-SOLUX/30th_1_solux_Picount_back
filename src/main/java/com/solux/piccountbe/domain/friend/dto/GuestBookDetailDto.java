package com.solux.piccountbe.domain.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GuestBookDetailDto {
    private Long guestbookId;
    private String writerProfileImage;
    private String writerNickname;
    private String content;
    private LocalDateTime createdAt;
}

