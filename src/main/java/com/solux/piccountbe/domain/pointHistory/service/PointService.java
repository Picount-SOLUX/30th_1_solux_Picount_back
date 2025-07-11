package com.solux.piccountbe.domain.pointHistory.service;

import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.pointHistory.entity.PointHistory;
import com.solux.piccountbe.domain.pointHistory.entity.Reason;
import com.solux.piccountbe.domain.pointHistory.repository.PointHistoryRepository;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    public void deductPoints(Member member, Long amount, Reason reason) {
        if (member.getPoint() < amount) {
            throw new CustomException(ErrorCode.NOT_ENOUGH_POINT);
        }

        member.usePoint(amount); // 엔티티에서 차감
        PointHistory history = new PointHistory(member, -amount, reason);
        pointHistoryRepository.save(history);
    }
}
