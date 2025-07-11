package com.solux.piccountbe.domain.pointHistory.entity;

import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.global.Timestamped;

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
public class PointHistory extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long pointId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(nullable = false)
	private Long amount;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private Reason reason;

	public PointHistory(Member member, Long amount, Reason reason) {
		this.member = member;
		this.amount = amount;
		this.reason = reason;
	}

}
