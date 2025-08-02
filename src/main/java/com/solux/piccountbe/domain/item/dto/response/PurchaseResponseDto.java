package com.solux.piccountbe.domain.item.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PurchaseResponseDto {
    private Long purchaseId;
    private Long itemId;
    private LocalDateTime purchasedAt;
}
