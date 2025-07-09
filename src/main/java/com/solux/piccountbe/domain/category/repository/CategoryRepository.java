package com.solux.piccountbe.domain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}