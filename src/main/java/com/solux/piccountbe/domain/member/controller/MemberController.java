package com.solux.piccountbe.domain.member.controller;

import com.solux.piccountbe.config.security.UserDetailsImpl;
import com.solux.piccountbe.domain.member.dto.*;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.member.service.MemberService;
import com.solux.piccountbe.global.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/findAccount")
    public ResponseEntity<Response<Long>> checkEmail(
            @RequestParam String email
    ) {
        Long memberId = memberService.checkEmail(email);
        return ResponseEntity.ok(Response.success("이메일로 회원 찾기 완료", memberId));
    }

    @PatchMapping("/findAccount/{memberId}")
    public ResponseEntity<Response<Void>> findAccount(
            @PathVariable Long memberId,
            @RequestBody findAccountRequestDto req
    ) {
        System.out.println(memberId + req.getPassword());
        memberService.findAccount(memberId, req.getPassword());
        System.out.println("ddd");
        return ResponseEntity.ok(Response.success("회원 찾기 및 비밀번호 재설정 완료", null));
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

    @PatchMapping("/visibility/main")
    public ResponseEntity<Response<Void>> updateMainVisibility(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody MainVisibilityRequestDto requestDto
    ) {
        Long memberId = userDetails.getMember().getMemberId();
        memberService.updateMainVisibility(memberId, requestDto.getIsMainVisible());
        return ResponseEntity.ok(Response.success("메인페이지 공개 여부가 변경되었습니다.", null));
    }

	@GetMapping("/friend-code")
	public ResponseEntity<Response<String>> getFriendCode(
			@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		String friendCode = memberService.getFriendCode(userDetails.getMember());
		return ResponseEntity.ok(Response.success("친구 코드 조회 성공", friendCode));
	}

}