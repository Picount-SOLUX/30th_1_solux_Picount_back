package com.solux.piccountbe.domain.member.entity;

import java.time.LocalDateTime;

import com.solux.piccountbe.global.Timestamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "token")
@NoArgsConstructor
public class Token extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long tokenId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn (name = "member_id", nullable = false)
	private Member member;

	@Column
	private String refreshToken;

	@Column
	private LocalDateTime expiresAt;
}
