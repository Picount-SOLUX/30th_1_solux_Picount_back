package com.solux.piccountbe.domain.budget.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.solux.piccountbe.domain.budget.dto.BudgetAllocationDto;
import com.solux.piccountbe.domain.budget.dto.BudgetResponseDto;
import com.solux.piccountbe.domain.budget.dto.GetAllBudgetResponseDto;
import com.solux.piccountbe.domain.budget.dto.GetBudgetResponseDto;
import com.solux.piccountbe.domain.budget.dto.UpdateBudgetAllocationDto;
import com.solux.piccountbe.domain.budget.entity.Budget;
import com.solux.piccountbe.domain.budget.entity.BudgetAllocation;
import com.solux.piccountbe.domain.budget.repository.BudgetAllocationRepository;
import com.solux.piccountbe.domain.budget.repository.BudgetRepository;
import com.solux.piccountbe.domain.category.entity.Category;
import com.solux.piccountbe.domain.category.entity.Type;
import com.solux.piccountbe.domain.category.service.CategoryService;
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

	private final MemberService memberService;
	private final CategoryService categoryService;
	private final BudgetRepository budgetRepository;
	private final BudgetAllocationRepository budgetAllocationRepository;

	public void createBudget(Long memberId, LocalDate startDate, LocalDate endDate, Integer totalAmount) {

		Member member = memberService.getMemberById(memberId);

		Budget beforeActiveBudget = budgetRepository
			.findByMemberAndActiveTrue(member)
			.orElse(null);

		if (beforeActiveBudget != null) {
			beforeActiveBudget.setActive(false);
			budgetRepository.save(beforeActiveBudget);
		}

		Budget budget = Budget.builder()
			.member(member)
			.startDate(startDate)
			.endDate(endDate)
			.active(true)
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
		Member member = memberService.getMemberById(memberId);
		boolean wasActive = budget.getActive();
		budgetRepository.delete(budget);
		if (wasActive) {
			budgetRepository.findTopByMemberOrderByStartDateDesc(member)
				.ifPresent(next -> {
					next.setActive(true);
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

		return getBudgetResponseDto(budget);
	}

	public GetAllBudgetResponseDto getAllBudget(Long memberId) {

		Member member = memberService.getMemberById(memberId);
		List<BudgetResponseDto> budgetResponseDtoList = member.getBudgets()
			.stream()
			.map(a -> new BudgetResponseDto(
				a.getBudgetId(),
				a.getStartDate(),
				a.getEndDate(),
				a.getTotalAmount(),
				a.getActive()
			))
			.collect(Collectors.toList());

		return new GetAllBudgetResponseDto(budgetResponseDtoList);

	}

	public GetBudgetResponseDto updateBudget(
		Long memberId,
		Long budgetId,
		LocalDate startDate,
		LocalDate endDate,
		Integer totalAmount,
		List<UpdateBudgetAllocationDto> budgetAllocationList
	) {
		Member member = memberService.getMemberById(memberId);
		Budget budget = budgetRepository.findById(budgetId)
			.orElseThrow(() -> new CustomException(ErrorCode.BUDGET_NOT_FOUND));

		if (!budget.getMember().getMemberId().equals(member.getMemberId())) {
			throw new CustomException(ErrorCode.BUDGET_NOT_MATCH_MEMBER);
		}

		Set<Long> categoryIdSet = new HashSet<>();
		int totalAllocationAmount = 0;
		for (UpdateBudgetAllocationDto budgetAllocation : budgetAllocationList) {
			if (!categoryIdSet.add(budgetAllocation.getCategoryId())) {
				throw new CustomException(ErrorCode.CATEGORY_DUPLICATE);
			}
			totalAllocationAmount += budgetAllocation.getAmount();

		}
		if (totalAllocationAmount > totalAmount) {
			throw new CustomException(ErrorCode.BUDGET_OVER);
		}

		budget.getBudgetAllocationList().clear();

		for (UpdateBudgetAllocationDto updateBudgetAllocation : budgetAllocationList) {

			Category category = categoryService.getCategoryById(memberId, updateBudgetAllocation.getCategoryId());

			if (category.getType() != Type.EXPENSE) {
				throw new CustomException(ErrorCode.CATEGORY_TYPE_NOT_EXPENSE);
			}

			BudgetAllocation budgetAllocation = BudgetAllocation.builder()
				.category(category)
				.budget(budget)
				.amount(updateBudgetAllocation.getAmount())
				.build();

			budgetAllocationRepository.save(budgetAllocation);
			budget.getBudgetAllocationList().add(budgetAllocation);
		}

		budget.updateBudget(startDate, endDate, totalAmount);
		budgetRepository.save(budget);

		return getBudgetResponseDto(budget);
	}

	public Long getActiveBudgetId(Member member) {
		return budgetRepository.findByMemberAndActiveTrue(member)
			.orElseThrow(() -> new CustomException(ErrorCode.BUDGET_NOT_FOUND))
			.getBudgetId();

	}

	private GetBudgetResponseDto getBudgetResponseDto(Budget budget) {
		List<BudgetAllocationDto> budgetAllocationDtoList = budget.getBudgetAllocationList()
			.stream()
			.map(a -> new BudgetAllocationDto(
				a.getAllocationId(),
				a.getCategory().getCategoryId(),
				a.getCategory().getName(),
				a.getAmount()
			))
			.collect(Collectors.toList());

		return new GetBudgetResponseDto(
			budget.getBudgetId(),
			budget.getStartDate(),
			budget.getEndDate(),
			budget.getTotalAmount(),
			budget.getActive(),
			budgetAllocationDtoList
		);
	}

}
