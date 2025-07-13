package com.solux.piccountbe.domain.pointHistory.repository;

import com.solux.piccountbe.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.pointHistory.entity.PointHistory;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    List<PointHistory> findByMemberOrderByCreatedAtDesc(Member member);
}
