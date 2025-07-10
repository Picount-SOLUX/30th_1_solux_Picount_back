package com.solux.piccountbe.domain.friend.service;

import com.solux.piccountbe.domain.friend.dto.FriendRequestDto;
import com.solux.piccountbe.domain.friend.entity.Friend;
import com.solux.piccountbe.domain.friend.entity.Status;
import com.solux.piccountbe.domain.friend.repository.FriendRepository;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.member.repository.MemberRepository;
//import com.solux.piccountbe.global.exception.CustomException;
//import com.solux.piccountbe.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;

    @Transactional
    public void requestFriend(Member loginMember, FriendRequestDto requestDto) {
        // TODO: 멤버 담당자가 findByFriendCode(String) 구현 후 사용
        // Member friendMember = memberRepository.findByFriendCode(requestDto.getFriendCode())
        //        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 임시 코드
        Member friendMember = null;

        // TODO: Member friendMember가 null이 아니어야 아래 코드가 동작함
        // Friend friend = new Friend(loginMember, friendMember, Status.APPROVAL);

        // friendRepository.save(friend);
    }
}