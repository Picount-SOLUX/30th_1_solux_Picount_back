package com.solux.piccountbe.domain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.category.entity.Category;
import com.solux.piccountbe.domain.category.entity.Type;
import com.solux.piccountbe.domain.member.entity.Member;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	boolean existsByMemberAndTypeAndName(Member member, Type type, String name);
}