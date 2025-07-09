package com.solux.piccountbe.domain.pointHistory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.pointHistory.entity.PointHistory;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
}
