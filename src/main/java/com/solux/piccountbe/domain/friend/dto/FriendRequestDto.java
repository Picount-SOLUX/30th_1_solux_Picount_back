package com.solux.piccountbe.domain.friend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendRequestDto {
    // 클라이언트가 보낸 친구 코드를 담기 위한 필드
    private String friendCode;
}