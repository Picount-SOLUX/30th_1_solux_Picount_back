package com.solux.piccountbe.domain.friend.controller;

import com.solux.piccountbe.domain.friend.dto.FriendRequestDto;
import com.solux.piccountbe.domain.friend.dto.FriendResponseDto;
import com.solux.piccountbe.domain.friend.dto.FriendApiResponse;
import com.solux.piccountbe.domain.friend.service.FriendService;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.config.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends")
public class FriendController {

    private final FriendService friendService;

    // 친구 요청
    @PostMapping("/request")
    public ResponseEntity<String> requestFriend(
            @RequestBody FriendRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Member loginMember = userDetails.getMember();
        friendService.requestFriend(loginMember, requestDto);
        return ResponseEntity.ok("친구 요청이 성공적으로 처리되었습니다.");
    }

    // 친구 조회 - 마이페이지
    @GetMapping("/my")
    public ResponseEntity<Map<String, Object>> getMyFriends(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Member loginMember = userDetails.getMember();
        List<FriendResponseDto> friendList = friendService.getMyFriends(loginMember);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "친구 목록을 성공적으로 조회했습니다.");
        response.put("data", friendList);

        return ResponseEntity.ok(response);
    }

    // 친구 삭제
    @DeleteMapping("/{friendId}")
    public ResponseEntity<FriendApiResponse> deleteFriend(
            @PathVariable Long friendId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws AccessDeniedException {
        Member loginMember = userDetails.getMember();
        friendService.deleteFriend(loginMember, friendId);
        return ResponseEntity.ok(new FriendApiResponse(true, "친구 관계가 삭제되었습니다."));
    }

}