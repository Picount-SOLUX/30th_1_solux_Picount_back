package com.solux.piccountbe.domain.challenge.repository;

import com.solux.piccountbe.domain.challenge.entity.Challenge;
import com.solux.piccountbe.domain.challenge.entity.Status;
import com.solux.piccountbe.domain.challenge.entity.Type;
import com.solux.piccountbe.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.challenge.entity.MemberChallenge;

import java.util.List;
import java.util.Optional;

public interface MemberChallengeRepository extends JpaRepository<MemberChallenge, Long> {
    List<MemberChallenge> findByMember(Member member);

    Optional<MemberChallenge> findByMemberAndChallenge(Member member, Challenge attendanceChallenge);

    List<MemberChallenge> findAllByChallenge_TypeInAndStatus(List<Type> attendance, Status status);
}
