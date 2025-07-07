package com.solux.piccountbe.domain.challenge.entity;

import java.time.LocalDate;

import com.solux.piccountbe.domain.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class MemberChallenge {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long memberChallengeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "challenge_id", nullable = false)
	private Challenge challenge;

	@Column
	private LocalDate completedAt;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private Status status;
}
