package com.solux.piccountbe.domain.budget.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.solux.piccountbe.domain.budget.dto.BudgetAllocationDto;
import com.solux.piccountbe.domain.budget.dto.GetBudgetResponseDto;
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

		Budget beforeActiveBudget = budgetRepository
			.findByMemberAndIsActiveTrue(member)
			.orElse(null);

		if (beforeActiveBudget != null) {
			beforeActiveBudget.setIsActive(false);
			budgetRepository.save(beforeActiveBudget);
		}

		Budget budget = Budget.builder()
			.member(member)
			.startDate(startDate)
			.endDate(endDate)
			.totalAmount(totalAmount)
			.isActive(true)
			.build();

		budgetRepository.save(budget);
	}

	public void deleteBudget(Long memberId, Long budgetId) {
		Budget budget = budgetRepository.findById(budgetId)
			.orElseThrow(() -> new CustomException(ErrorCode.BUDGET_NOT_FOUND));

		if (budget.getMember().getMemberId() != memberId) {
			throw new CustomException(ErrorCode.BUDGET_NOT_MATCH_MEMBER);
		}
		Member member = memberService.getMemberById(memberId);
		boolean wasActive = budget.getIsActive();
		budgetRepository.delete(budget);
		if (wasActive) {
			budgetRepository.findTopByMemberOrderByStartDateDesc(member)
				.ifPresent(next -> {
					next.setIsActive(true);
					budgetRepository.save(next);
				});
		}
	}

	public GetBudgetResponseDto getBudget(Member member, Long budgetId) {
		Budget budget = budgetRepository.findById(budgetId)
			.orElseThrow(() -> new CustomException(ErrorCode.BUDGET_NOT_FOUND));

		if (!budget.getMember().getMemberId().equals(member.getMemberId())) {
			throw new CustomException(ErrorCode.BUDGET_NOT_MATCH_MEMBER);
		}

		List<BudgetAllocationDto> budgetAllocationDtoList = budget.getBudgetAllocationList()
			.stream()
			.map(a -> new BudgetAllocationDto(
				a.getAllocationId(),
				a.getCategory().getCategoryId(),
				a.getCategory().getName(),
				a.getAmount()
			))
			.collect(Collectors.toList());

		GetBudgetResponseDto getBudgetResponseDto = new GetBudgetResponseDto(
			budgetId,
			budget.getStartDate(),
			budget.getEndDate(),
			budget.getTotalAmount(),
			budget.getIsActive(),
			budgetAllocationDtoList
		);
		return getBudgetResponseDto;
	}

	public Long getActiveBudgetId(Member member) {
		return budgetRepository.findByMemberAndIsActiveTrue(member)
			.orElseThrow(() -> new CustomException(ErrorCode.BUDGET_NOT_FOUND))
			.getBudgetId();

	}

}
