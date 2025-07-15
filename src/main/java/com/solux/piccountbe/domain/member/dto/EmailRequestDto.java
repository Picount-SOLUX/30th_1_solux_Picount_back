package com.solux.piccountbe.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailRequestDto {
	@NotBlank(message = "이메일을 입력해주십시오.")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
		message = "이메일을 형식에 맞게 입력해주십시오.")
	String email;
}
