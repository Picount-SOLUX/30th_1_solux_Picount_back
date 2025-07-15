package com.solux.piccountbe.domain.budget.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.solux.piccountbe.domain.budget.entity.Budget;
import com.solux.piccountbe.domain.budget.repository.BudgetRepository;
import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.domain.member.service.MemberService;

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
}
