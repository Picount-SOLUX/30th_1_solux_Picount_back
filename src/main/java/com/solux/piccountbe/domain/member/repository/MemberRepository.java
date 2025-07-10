package com.solux.piccountbe.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByEmail(String email);

	Optional<Member> findByFriendCode(String friendCode);

	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);

	boolean existsByFriendCode(String friendCode);
}
