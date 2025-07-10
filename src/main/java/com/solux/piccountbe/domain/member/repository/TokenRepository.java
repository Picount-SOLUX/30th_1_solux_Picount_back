package com.solux.piccountbe.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.member.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
	Optional<Token> findByRefreshToken(String RefreshToken);
}

