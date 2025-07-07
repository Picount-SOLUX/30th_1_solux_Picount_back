package com.solux.piccountbe.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
