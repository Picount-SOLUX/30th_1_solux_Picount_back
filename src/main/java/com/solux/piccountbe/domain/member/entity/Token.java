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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long tokenId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column
	@Setter
	private String refreshToken;

	@Column
	@Setter
	private LocalDateTime expiresAt;

	@Builder
	public Token(Member member, String refreshToken, LocalDateTime expiresAt) {
		this.member = member;
		this.refreshToken = refreshToken;
		this.expiresAt = expiresAt;
	}

	public Token update(String newRefreshToken, LocalDateTime expiresAt) {
		this.refreshToken = newRefreshToken;
		this.expiresAt = expiresAt;
		return this;
	}
}
