package com.solux.piccountbe.domain.friend.service;

import com.solux.piccountbe.domain.friend.dto.FriendRequestDto;
import com.solux.piccountbe.domain.friend.entity.Friend;
import com.solux.piccountbe.domain.friend.entity.Status;
import com.solux.piccountbe.domain.friend.repository.FriendRepository;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final MemberService memberService; // ✅ 멤버 서비스 주입

    @Transactional
    public void requestFriend(Member loginMember, FriendRequestDto requestDto) {

        // friendCode로 친구 멤버 조회
        Member friendMember = memberService.findByFriendCode(requestDto.getFriendCode());

        // 친구 관계 생성
        Friend friend = new Friend(loginMember, friendMember, Status.APPROVAL);

        friendRepository.save(friend);
    }
}
