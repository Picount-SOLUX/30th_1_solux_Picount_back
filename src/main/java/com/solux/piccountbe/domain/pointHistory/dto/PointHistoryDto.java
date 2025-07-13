package com.solux.piccountbe.domain.pointHistory.dto;

import com.solux.piccountbe.domain.pointHistory.entity.PointHistory;
import com.solux.piccountbe.domain.pointHistory.entity.Reason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PointHistoryDto {
    private Long pointId;
    private String amount; // +,- 포함해서 응답을 위해 string
    private Reason reason;
    private LocalDateTime createdAt;

    public PointHistoryDto(Long pointId, Long amount, Reason reason, LocalDateTime createdAt) {
        this.pointId = pointId;
        this.amount = formatAmount(amount);
        this.reason = reason;
        this.createdAt = createdAt;
    }

    // 양수면 + 붙여서 응답하게 수정
    private String formatAmount(Long amount) {
        if (amount > 0) {
            return "+" + amount;
        } else {
            return amount.toString();
        }
    }

    public static PointHistoryDto from(PointHistory pointHistory) {
        return new PointHistoryDto(
                pointHistory.getPointId(),
                pointHistory.getAmount(),
                pointHistory.getReason(),
                pointHistory.getCreatedAt()
        );
    }
}

