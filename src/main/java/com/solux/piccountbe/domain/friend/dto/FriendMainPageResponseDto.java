package com.solux.piccountbe.domain.friend.dto;

import com.solux.piccountbe.domain.budget.dto.BudgetAllocationDto;
import com.solux.piccountbe.domain.friend.dto.GuestBookSummaryDto;
import com.solux.piccountbe.domain.callendar.dto.EmotionRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FriendMainPageResponseDto {
    private String nickname; // 이름
    private String profileImageUrl; // 프로필 이미지
    private List<BudgetAllocationDto> budgetCategoryStats; // 예산 그래프에 활용
    private List<GuestBookSummaryDto> guestBooks; // 방명록
    private List<EmotionRequestDto> emotionStickers; // 감정 스티커
}
