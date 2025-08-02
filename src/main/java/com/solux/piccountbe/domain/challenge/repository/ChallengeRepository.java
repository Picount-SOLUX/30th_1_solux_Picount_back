package com.solux.piccountbe.domain.challenge.repository;

import com.solux.piccountbe.domain.challenge.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.challenge.entity.Challenge;

import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Optional<Challenge> findByType(Type type);
}
