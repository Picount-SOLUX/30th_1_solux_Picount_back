package com.solux.piccountbe.domain.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import com.solux.piccountbe.domain.member.dto.MemberGroupTypeRequestDto;
import com.solux.piccountbe.domain.member.dto.PasswordRequestDto;
import com.solux.piccountbe.domain.member.dto.ProfileResponseDto;
import com.solux.piccountbe.domain.member.dto.ProfileUpdateRequestDto;
import com.solux.piccountbe.domain.member.dto.RefreshRequestDto;
import com.solux.piccountbe.domain.member.dto.SignupRequestDto;
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
	public ResponseEntity<Response<ProfileResponseDto>> signup(@RequestBody @Valid SignupRequestDto req) {
		ProfileResponseDto res = memberService.signup(req);
		return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("회원가입 성공", res));
	}

	@PostMapping("/login")
	public ResponseEntity<Response<LoginResponseDto>> login(@RequestBody LoginRequestDto req) {
		LoginResponseDto res = memberService.login(req);
		return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("로그인 성공", res));
	}

	@PostMapping("/refresh")
	public ResponseEntity<Response<LoginResponseDto>> refresh(@RequestBody RefreshRequestDto req) {
		LoginResponseDto res = memberService.refresh(req.getRefreshToken());
		return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("토큰 재발급 완료", res));
	}

	@PostMapping("/logout")
	public ResponseEntity<Response<Void>> logout(
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Long memberId = userDetails.getMember().getMemberId();
		memberService.logout(memberId);
		return ResponseEntity.ok(Response.success("로그아웃 완료", null));
	}

	@PatchMapping("/withdraw")
	public ResponseEntity<Response<Void>> withdraw(
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Long memberId = userDetails.getMember().getMemberId();
		memberService.withdraw(memberId);
		return ResponseEntity.ok(Response.success("회원탈퇴 완료", null));
	}

	@PatchMapping("/password")
	public ResponseEntity<Response<Void>> updatePassword(
		@RequestBody @Valid PasswordRequestDto req,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Long memberId = userDetails.getMember().getMemberId();
		memberService.updatePassword(memberId, req.getPrePassword(), req.getNewPassword());
		return ResponseEntity.ok(Response.success("비밀번호 변경 완료", null));
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
	public ResponseEntity<Response<ProfileResponseDto>> updateProfile(
		@RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
		@Valid @RequestPart(value = "profileInfo") ProfileUpdateRequestDto profileUpdateRequestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {

		Long memberId = userDetails.getMember().getMemberId();
		ProfileResponseDto res = memberService.updateProfile(memberId, profileImage, profileUpdateRequestDto);
		return ResponseEntity.ok(Response.success("마이프로필 수정 성공", res));
	}

	@PutMapping(value = "/membergrouptype")
	public ResponseEntity<Response<ProfileResponseDto>> updateMemberGroupType(
		@RequestBody MemberGroupTypeRequestDto memberGroupTypeRequestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Long memberId = userDetails.getMember().getMemberId();
		ProfileResponseDto res = memberService.updateMemberGroupType(memberId, memberGroupTypeRequestDto.getMemberGroupType());
		return ResponseEntity.ok(Response.success("직군 변경 성공", res));
	}

}