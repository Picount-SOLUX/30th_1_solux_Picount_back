package com.solux.piccountbe.domain.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.solux.piccountbe.config.security.UserDetailsImpl;
import com.solux.piccountbe.domain.member.dto.EmailRequestDto;
import com.solux.piccountbe.domain.member.dto.LoginRequestDto;
import com.solux.piccountbe.domain.member.dto.LoginResponseDto;
import com.solux.piccountbe.domain.member.dto.ProfileResponseDto;
import com.solux.piccountbe.domain.member.dto.ProfileUpdateRequestDto;
import com.solux.piccountbe.domain.member.dto.SignupRequestDto;
import com.solux.piccountbe.domain.member.dto.MemberGroupTypeRequestDto;
import com.solux.piccountbe.domain.member.entity.Member;
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

	@GetMapping("/profile")
	public ResponseEntity<Response<ProfileResponseDto>> getProfile(
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Member member = userDetails.getMember();
		ProfileResponseDto res = memberService.getProfile(member);
		return ResponseEntity.ok(Response.success("마이프로필 조회 성공", res));
	}

	@PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Response<Void>> updateProfile(
		@RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
		@Valid @RequestPart(value = "profileInfo") ProfileUpdateRequestDto profileUpdateRequestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {

		Long memberId = userDetails.getMember().getMemberId();
		memberService.updateProfile(memberId, profileImage, profileUpdateRequestDto);
		return ResponseEntity.ok(Response.success("마이프로필 수정 성공", null));
	}

	@PutMapping(value = "/membergrouptype")
	public ResponseEntity<Response<Void>> updateMemberGroupType(
		@RequestBody MemberGroupTypeRequestDto memberGroupTypeRequestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Long memberId = userDetails.getMember().getMemberId();
		memberService.updateMemberGroupType(memberId, memberGroupTypeRequestDto.getMemberGroupType());
		return ResponseEntity.ok(Response.success("직군 변경 성공", null));
	}

	@PutMapping(value = "/id")
	public ResponseEntity<Response<Void>> updateEmail(
		@RequestBody @Valid EmailRequestDto emailRequestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Long memberId = userDetails.getMember().getMemberId();
		memberService.updateEmail(memberId, emailRequestDto.getEmail());
		return ResponseEntity.ok(Response.success("직군 변경 성공", null));
	}

}