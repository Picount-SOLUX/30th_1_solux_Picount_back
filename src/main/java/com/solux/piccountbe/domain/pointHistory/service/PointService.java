package com.solux.piccountbe.domain.pointHistory.service;

import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.member.service.MemberService;
import com.solux.piccountbe.domain.pointHistory.dto.MyPointHistoryResponseDto;
import com.solux.piccountbe.domain.pointHistory.dto.MyPointResponseDto;
import com.solux.piccountbe.domain.pointHistory.entity.PointHistory;
import com.solux.piccountbe.domain.pointHistory.entity.Reason;
import com.solux.piccountbe.domain.pointHistory.repository.PointHistoryRepository;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointHistoryRepository pointHistoryRepository;
    private final MemberService memberService;

    // 내 포인트 조회
    @Transactional
    public MyPointResponseDto getMyPoint(Member member) {
        Member findMember = memberService.getMemberById(member.getMemberId());
        return MyPointResponseDto.from(findMember);
    }

    // 내 포인트 내역 조회
    @Transactional
    public MyPointHistoryResponseDto getMyPointHistory(Member member) {
        Member findMember = memberService.getMemberById(member.getMemberId());
        List<PointHistory> histories = pointHistoryRepository.findByMemberOrderByCreatedAtDesc(findMember);
        return MyPointHistoryResponseDto.of(findMember, histories);
    }

    // 포인트 차감
    @Transactional
    public void deductPoints(Member member, Long amount, Reason reason) {
        if (member.getPoint() < amount) {
            throw new CustomException(ErrorCode.NOT_ENOUGH_POINT);
        }

        member.usePoint(amount); // 엔티티에서 차감
        PointHistory history = new PointHistory(member, -amount, reason);
        pointHistoryRepository.save(history);
    }

    // 챌린지 - 포인트 지급
    public void saveChallengeRewardHistory(Member member, long reward, Reason reason) {
        PointHistory history = new PointHistory(
                member,
                reward,
                reason
        );
        pointHistoryRepository.save(history);
    }
}
