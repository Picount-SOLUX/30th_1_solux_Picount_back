package com.solux.piccountbe.domain.friend.service;

import com.solux.piccountbe.domain.callendar.service.EmotionService;
import com.solux.piccountbe.domain.friend.dto.FriendRequestDto;
import com.solux.piccountbe.domain.friend.dto.FriendResponseDto;
import com.solux.piccountbe.domain.friend.dto.FriendMainResponseDto ;
import com.solux.piccountbe.domain.friend.entity.Friend;
import com.solux.piccountbe.domain.friend.entity.Status;
import com.solux.piccountbe.domain.member.repository.MemberRepository;
import com.solux.piccountbe.domain.friend.repository.FriendRepository;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.member.service.MemberService;
import com.solux.piccountbe.domain.friend.dto.FriendMainPageResponseDto;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;
import com.solux.piccountbe.domain.budget.dto.BudgetAllocationDto;
import com.solux.piccountbe.domain.friend.dto.GuestBookSummaryDto;
import com.solux.piccountbe.domain.callendar.dto.EmotionRequestDto;
import com.solux.piccountbe.domain.budget.service.BudgetService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService; // 멤버 서비스
    private final BudgetService budgetService;
    private final GuestBookService guestBookService;
    private final EmotionService emotionService;

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

    // 메인페이지 친구리스트 조회
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

    // 친구 메인 페이지 조회
    @Transactional(readOnly = true)
    public FriendMainPageResponseDto getFriendMainPage(Long viewerId, Long ownerId) {
        Member viewer = memberService.getMemberById(viewerId);
        Member owner = memberService.getMemberById(ownerId);

        // 친구 관계 확인
        boolean isFriend = friendRepository.existsByMemberAndFriendMemberAndStatus(viewer, owner, Status.APPROVAL)
                || friendRepository.existsByMemberAndFriendMemberAndStatus(owner, viewer, Status.APPROVAL);

        if (!isFriend || !owner.getIsMainVisible()) {
            throw new CustomException(ErrorCode.FRIEND_ACCESS_DENIED);
        }

        // 예산 통계
        //List<BudgetAllocationDto> budgetStats = getActiveBudgetAllocations(owner);
        List<BudgetAllocationDto> budgetStats = Collections.emptyList();

        // 방명록 요약 조회
        Pageable top3 = PageRequest.of(0, 3);
        List<GuestBookSummaryDto> guestBooks = guestBookService.getGuestBooks(viewer, ownerId, top3).getContent();

        // 감정 스티커
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        List<EmotionRequestDto> emotions = emotionService.getEmotionStickers(ownerId, year, month);

        return FriendMainPageResponseDto.builder()
                .nickname(owner.getNickname())
                .profileImageUrl(owner.getProfileImageUrl())
                .budgetCategoryStats(budgetStats)
                .guestBooks(guestBooks)
                .emotionStickers(emotions)
                .build();
    }

    // 예산 할당 데이터 가져오기
    private List<BudgetAllocationDto> getActiveBudgetAllocations(Member owner) {
        try {
            Long budgetId = budgetService.getActiveBudgetId(owner); // 예외 발생 가능 지점
            return budgetService.getBudget(owner, budgetId).getBudgetAllocationList();
        } catch (CustomException e) {
            if (e.getErrorCode() == ErrorCode.BUDGET_NOT_FOUND) {
                // 예산이 없으면 빈 리스트 반환
                return Collections.emptyList();
            }
            // 그 외 예외는 다시 던짐
            throw e;
        }
    }
}