package com.solux.piccountbe.domain.budget.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.budget.entity.BudgetAllocation;

public interface BudgetAllocationRepository extends JpaRepository<BudgetAllocation, Long> {
}
