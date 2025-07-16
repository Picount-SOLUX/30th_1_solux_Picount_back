package com.solux.piccountbe.domain.budget.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.budget.entity.Budget;
import com.solux.piccountbe.domain.member.entity.Member;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

	Optional<Budget> findByMemberAndIsActiveTrue(Member member);
	Optional<Budget> findTopByMemberOrderByStartDateDesc(Member member);
}
