package com.solux.piccountbe.domain.callendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.callendar.entity.ExpenseDetail;

public interface ExpenseDetailRepository extends JpaRepository<ExpenseDetail, Long> {
}


