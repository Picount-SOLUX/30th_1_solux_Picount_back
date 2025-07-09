package com.solux.piccountbe.domain.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.challenge.entity.Challenge;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}
