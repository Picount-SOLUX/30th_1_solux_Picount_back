package com.solux.piccountbe.domain.item.repository;

import com.solux.piccountbe.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.item.entity.Purchase;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByMember(Member member);
}
