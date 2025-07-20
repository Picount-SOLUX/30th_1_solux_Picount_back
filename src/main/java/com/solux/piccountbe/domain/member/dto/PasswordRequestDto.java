package com.solux.piccountbe.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordRequestDto {

	private String prePassword;

	@NotBlank(message = "새로운 비밀번호를 입력해주십시오.")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,15}$",
		message = "비밀번호는 최소 6자 이상, 15자 이하이며 알파벳 대소문자, 숫자,특수문자를 각각 1자 이상 포함해야 합니다.")
	private String newPassword;
}
