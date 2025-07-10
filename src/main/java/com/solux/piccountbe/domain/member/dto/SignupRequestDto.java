package com.solux.piccountbe.domain.member.dto;

import com.solux.piccountbe.domain.member.entity.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequestDto {

	@NotBlank(message = "이메일을 입력해주십시오.")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
		message = "이메일을 형식에 맞게 입력해주십시오.")
	private String email;

	@NotBlank(message = "비밀번호를 입력해주십시오.")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,15}$",
		message = "비밀번호는 최소 6자 이상, 15자 이하이며 알파벳 대소문자, 숫자,특수문자를 각각 1자 이상 포함해야 합니다.")
	private String password;

	@NotBlank(message = "닉네임을 입력해주십시오.")
	private String nickname;

	private Gender gender;

	private Integer age;

}
