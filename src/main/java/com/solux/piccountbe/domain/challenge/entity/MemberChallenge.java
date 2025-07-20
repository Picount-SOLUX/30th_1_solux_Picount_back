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

	public MemberChallenge(Member member, Challenge challenge, Status status) {
		this.member = member;
		this.challenge = challenge;
		this.status = status;
		this.completedAt = null;
	}

	public void complete() {
		this.status = Status.COMPLETED;
		this.completedAt = LocalDate.now();
	}

	public void resetDaily() {
		if (this.challenge.getType() == Type.ATTENDANCE || this.challenge.getType() == Type.GUESTBOOK) {
			this.status = Status.LOCKED;
			this.completedAt = null;
		}
	}

	public void updateStatus(Status status) {
		this.status = status;
	}

	public void setCompletedAt(LocalDate now) {
		this.completedAt = now;
	}
}
