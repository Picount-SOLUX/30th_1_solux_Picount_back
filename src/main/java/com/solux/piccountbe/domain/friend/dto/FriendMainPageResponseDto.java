package com.solux.piccountbe.domain.friend.dto;

import com.solux.piccountbe.domain.budget.dto.BudgetAllocationDto;
import com.solux.piccountbe.domain.friend.dto.GuestBookSummaryDto;
import com.solux.piccountbe.domain.callendar.dto.EmotionRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class FriendMainPageResponseDto {
    private String nickname;
    private String profileImageUrl;
    private List<BudgetAllocationDto> budgetCategoryStats;
    private List<GuestBookSummaryDto> guestBooks; // 요약
    private List<GuestBookDetailDto> guestBookDetails; // 상세
    private List<EmotionRequestDto> emotionStickers;
}
