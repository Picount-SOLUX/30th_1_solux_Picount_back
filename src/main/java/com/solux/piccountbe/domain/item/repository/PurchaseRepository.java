package com.solux.piccountbe.domain.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.item.entity.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
