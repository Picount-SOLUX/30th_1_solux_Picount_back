package com.solux.piccountbe.domain.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendMainResponseDto {
    private Long memberId;
    private String nickname;
    private String profileImageUrl;
    private String intro;
}
