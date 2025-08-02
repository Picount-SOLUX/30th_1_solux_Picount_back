package com.solux.piccountbe.domain.callendar.dto;

import com.solux.piccountbe.domain.callendar.entity.EmotionType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmotionRequestDto {
    @NotNull(message = "날짜는 필수입니다.")
    private String entryDate; // yyyy-MM-dd

    @NotNull(message = "감정은 필수입니다.")
    private EmotionType emotion;
}
