package com.solux.piccountbe.domain.callendar.dto;

import com.solux.piccountbe.domain.callendar.entity.EmotionType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmotionRequestDto {

    @NotNull(message = "날짜는 필수입니다.")
    private String entryDate; // yyyy-MM-dd

    @NotNull(message = "감정은 필수입니다.")
    private EmotionType emotion;
}
