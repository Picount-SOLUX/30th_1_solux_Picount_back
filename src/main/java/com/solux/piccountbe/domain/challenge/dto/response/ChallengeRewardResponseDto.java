package com.solux.piccountbe.domain.challenge.dto.response;

import com.solux.piccountbe.domain.challenge.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChallengeRewardResponseDto {
    private Long challengeId;
    private Status status;
    private long rewardPoint;
}

