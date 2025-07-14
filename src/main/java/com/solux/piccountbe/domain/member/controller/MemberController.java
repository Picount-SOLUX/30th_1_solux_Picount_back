package com.solux.piccountbe.domain.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solux.piccountbe.domain.member.dto.LoginRequestDto;
import com.solux.piccountbe.domain.member.dto.LoginResponseDto;
import com.solux.piccountbe.domain.member.dto.SignupRequestDto;
import com.solux.piccountbe.domain.member.service.MemberService;
import com.solux.piccountbe.global.Response;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/signup")
	public ResponseEntity<Response<Void>> signup(@RequestBody @Valid SignupRequestDto req) {
		memberService.signup(req);
		return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("회원가입 성공", null));
	}

	@PostMapping("/login")
	public ResponseEntity<Response<LoginResponseDto>> login(@RequestBody LoginRequestDto req) {
		LoginResponseDto res = memberService.login(req);
		return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("로그인 성공", res));
	}

}