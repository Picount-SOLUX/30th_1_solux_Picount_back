package com.solux.piccountbe.domain.friend.controller;

import com.solux.piccountbe.domain.friend.dto.FriendRequestDto;
import com.solux.piccountbe.domain.friend.service.FriendService;
import com.solux.piccountbe.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends")
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/request")
    public ResponseEntity<String> requestFriend(
            @RequestBody FriendRequestDto requestDto
            // , @AuthenticationPrincipal Member loginMember
    ) {
        // TODO: 인증된 사용자 정보 받아오면 주석 해제
        Member loginMember = null; // 임시 처리

        friendService.requestFriend(loginMember, requestDto);

        return ResponseEntity.ok("친구 요청이 성공적으로 처리되었습니다.");
    }
}
