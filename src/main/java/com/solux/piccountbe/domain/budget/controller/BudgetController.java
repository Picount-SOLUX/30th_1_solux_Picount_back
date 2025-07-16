package com.solux.piccountbe.domain.budget.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solux.piccountbe.config.security.UserDetailsImpl;
import com.solux.piccountbe.domain.budget.dto.CreateBudgetRequestDto;
import com.solux.piccountbe.domain.budget.dto.GetBudgetResponseDto;
import com.solux.piccountbe.domain.budget.service.BudgetService;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.global.Response;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/budgets")
public class BudgetController {

	private final BudgetService budgetService;

	@PostMapping
	public ResponseEntity<Response<Void>> createBudget(
		@RequestBody CreateBudgetRequestDto req,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Long memberId = userDetails.getMember().getMemberId();
		budgetService.createBudget(memberId, req.getStartDate(), req.getEndDate(), req.getTotalAmount());
		return ResponseEntity.ok(Response.success("예산 생성 완료", null));
	}

	@DeleteMapping("/{budgetId}")
	public ResponseEntity<Response<Void>> deleteBudget(
		@PathVariable Long budgetId,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Long memberId = userDetails.getMember().getMemberId();
		budgetService.deleteBudget(memberId, budgetId);
		return ResponseEntity.ok(Response.success("예산 삭제 완료", null));
	}

	@GetMapping("/{budgetId}")
	public ResponseEntity<Response<GetBudgetResponseDto>> getBudget(
		@PathVariable Long budgetId,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Member member = userDetails.getMember();
		GetBudgetResponseDto res = budgetService.getBudget(member, budgetId);
		return ResponseEntity.ok(Response.success("예산 개별조회 완료", res));
	}

	@GetMapping("/active")
	public ResponseEntity<Response<GetBudgetResponseDto>> getBudget(
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		Member member = userDetails.getMember();
		Long budgetId = budgetService.getActiveBudgetId(member);
		GetBudgetResponseDto res = budgetService.getBudget(member, budgetId);
		return ResponseEntity.ok(Response.success("현재 활성화된 예산 개별조회 완료", res));
	}

}
