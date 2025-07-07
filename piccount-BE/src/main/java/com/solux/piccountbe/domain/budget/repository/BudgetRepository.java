package com.solux.piccountbe.domain.budget.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.budget.entity.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
}
