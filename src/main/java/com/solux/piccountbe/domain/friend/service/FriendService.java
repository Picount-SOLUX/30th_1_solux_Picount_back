package com.solux.piccountbe.domain.friend.service;

import com.solux.piccountbe.domain.friend.dto.FriendRequestDto;
import com.solux.piccountbe.domain.friend.dto.FriendResponseDto;
import com.solux.piccountbe.domain.friend.dto.FriendMainResponseDto ;
import com.solux.piccountbe.domain.friend.entity.Friend;
import com.solux.piccountbe.domain.friend.entity.Status;
import com.solux.piccountbe.domain.member.repository.MemberRepository;
import com.solux.piccountbe.domain.friend.repository.FriendRepository;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService; // 멤버 서비스

    // 친구 신청
    @Transactional
    public void requestFriend(Member loginMember, FriendRequestDto requestDto) {

        // friendCode로 친구 멤버 조회
        Member friendMember = memberService.findByFriendCode(requestDto.getFriendCode());
        // 친구 관계 생성
        Friend friend = new Friend(loginMember, friendMember, Status.APPROVAL);
        friendRepository.save(friend);
    }

    // 마이페이지 친구 조회
    @Transactional(readOnly = true)
    public List<FriendResponseDto> getMyFriends(Member loginMember) {
        List<Friend> friends = friendRepository.findByMemberOrFriendMemberAndStatus(
                loginMember, loginMember, Status.APPROVAL
        );

        return friends.stream().map(friend -> {
            Member other = friend.getMember().getMemberId().equals(loginMember.getMemberId())
                    ? friend.getFriendMember()
                    : friend.getMember();
            return new FriendResponseDto(
                    other.getMemberId(),
                    other.getNickname(),
                    other.getProfileImageUrl()
            );
        }).collect(Collectors.toList());
    }

    // 친구 삭제
    @Transactional
    public void deleteFriend(Member loginMember, Long friendId) throws AccessDeniedException {
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new IllegalArgumentException("친구 관계를 찾을 수 없습니다."));

        log.info("로그인한 유저 ID: {}", loginMember.getMemberId());
        log.info("Friend 관계 memberId: {}", friend.getMember().getMemberId());
        log.info("Friend 관계 friendMemberId: {}", friend.getFriendMember().getMemberId());

        if (!friend.getMember().getMemberId().equals(loginMember.getMemberId()) &&
                !friend.getFriendMember().getMemberId().equals(loginMember.getMemberId())) {
            throw new AccessDeniedException("해당 친구 관계에 접근할 수 없습니다.");
        }

        friendRepository.delete(friend);
    }

    // 메인페이지 친구 조회
    @Transactional(readOnly = true)
    public List<FriendMainResponseDto> getFriendsForMain(Member loginMember) {
        List<Friend> friends = friendRepository.findByMemberOrFriendMemberAndStatus(
                loginMember, loginMember, Status.APPROVAL
        );

        return friends.stream()
                .map(friend -> {
                    Member other = friend.getMember().getMemberId().equals(loginMember.getMemberId())
                            ? friend.getFriendMember()
                            : friend.getMember();

                    return new FriendMainResponseDto(
                            other.getMemberId(),
                            other.getNickname(),
                            other.getProfileImageUrl(),
                            other.getIntro() // 한줄소개
                    );
                })
                .collect(Collectors.toList());
    }

}
