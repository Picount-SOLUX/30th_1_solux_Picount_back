package com.solux.piccountbe.domain.challenge.dto.response;

import com.solux.piccountbe.domain.challenge.entity.MemberChallenge;
import com.solux.piccountbe.domain.challenge.entity.Status;
import com.solux.piccountbe.domain.challenge.entity.Type;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MemberChallengeResponseDto {
    private Long challengeId;
    private String name;
    private Type type;
    private Status status;
    private LocalDate completedAt;

    public static MemberChallengeResponseDto from(MemberChallenge mc) {
        return MemberChallengeResponseDto.builder()
                .challengeId(mc.getChallenge().getChallengeId())
                .name(mc.getChallenge().getName())
                .type(mc.getChallenge().getType())
                .status(mc.getStatus())
                .completedAt(mc.getCompletedAt())
                .build();
    }
}

