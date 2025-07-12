package com.solux.piccountbe.domain.pointHistory.dto;

import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.pointHistory.entity.PointHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MyPointHistoryResponseDto {
    private Long currentPoint;
    private List<PointHistoryDto> history;

    public static MyPointHistoryResponseDto of(Member member, List<PointHistory> histories) {
        List<PointHistoryDto> historyDtos = histories.stream()
                .map(PointHistoryDto::from)
                .toList();

        return new MyPointHistoryResponseDto(member.getPoint(), historyDtos);
    }
}
