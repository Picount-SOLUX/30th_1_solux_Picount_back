package com.solux.piccountbe.domain.challenge.dto.response;

import com.solux.piccountbe.domain.challenge.entity.Challenge;
import com.solux.piccountbe.domain.challenge.entity.Type;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChallengeResponseDto {
    private Long challengeId;
    private String name;
    private Type type;

    public static ChallengeResponseDto from(Challenge challenge) {
        return ChallengeResponseDto.builder()
                .challengeId(challenge.getChallengeId())
                .name(challenge.getName())
                .type(challenge.getType())
                .build();
    }
}

