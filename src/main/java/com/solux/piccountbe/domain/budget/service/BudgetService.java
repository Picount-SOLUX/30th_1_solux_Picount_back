package com.solux.piccountbe.domain.budget.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.solux.piccountbe.domain.budget.entity.Budget;
import com.solux.piccountbe.domain.budget.repository.BudgetRepository;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.member.service.MemberService;
import com.solux.piccountbe.global.exception.CustomException;
import com.solux.piccountbe.global.exception.ErrorCode;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class BudgetService {

	private final BudgetRepository budgetRepository;
	private final MemberService memberService;

	public void createBudget(Long memberId, LocalDate startDate, LocalDate endDate, int totalAmount) {

		Member member = memberService.getMemberById(memberId);
		Budget budget = Budget.builder()
			.member(member)
			.startDate(startDate)
			.endDate(endDate)
			.totalAmount(totalAmount)
			.build();

		budgetRepository.save(budget);
	}

	public void deleteBudget(Long memberId, Long budgetId) {
		Budget budget = budgetRepository.findById(budgetId)
			.orElseThrow(() -> new CustomException(ErrorCode.BUDGET_NOT_FOUND));

		if (budget.getMember().getMemberId() != memberId) {
			throw new CustomException(ErrorCode.BUDGET_NOT_MATCH_MEMBER);
		}

		budgetRepository.delete(budget);
	}
}
