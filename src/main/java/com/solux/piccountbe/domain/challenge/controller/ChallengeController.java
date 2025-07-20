package com.solux.piccountbe.domain.challenge.controller;

import com.solux.piccountbe.config.security.UserDetailsImpl;
import com.solux.piccountbe.domain.challenge.dto.response.ChallengeResponseDto;
import com.solux.piccountbe.domain.challenge.dto.response.ChallengeRewardResponseDto;
import com.solux.piccountbe.domain.challenge.dto.response.MemberChallengeResponseDto;
import com.solux.piccountbe.domain.challenge.service.ChallengeService;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.global.Response;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    // 내 챌린지 현황
    @GetMapping("/me")
    public ResponseEntity<Response<List<MemberChallengeResponseDto>>> getMyChallenges(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (userDetails == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        Long memberId = userDetails.getMember().getMemberId();
        challengeService.evaluateAllChallenges(memberId);
        List<MemberChallengeResponseDto> challenges = challengeService.getMyChallengeStatus(memberId);
        return ResponseEntity.ok(Response.success("나의 챌린지 현황 조회 성공", challenges));
    }

    // 전체 챌린지 목록
    @GetMapping
    public ResponseEntity<Response<List<ChallengeResponseDto>>> getAllChallenges(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (userDetails == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        List<ChallengeResponseDto> challenges = challengeService.getAllChallenges();
        return ResponseEntity.ok(Response.success("전체 챌린지 목록 조회 성공", challenges));
    }

    // 챌린지 보상 수령
    @PostMapping("/{challengeId}/complete")
    public ResponseEntity<Response<ChallengeRewardResponseDto>> completeChallenge(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long challengeId
    ) {
        if (userDetails == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        Member member = userDetails.getMember();
        ChallengeRewardResponseDto response = challengeService.completeChallenge(member, challengeId);
        return ResponseEntity.ok(Response.success("챌린지 완료 처리 성공", response));
    }

}
