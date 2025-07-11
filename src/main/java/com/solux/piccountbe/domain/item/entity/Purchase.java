package com.solux.piccountbe.domain.item.entity;

import java.time.LocalDateTime;

import com.solux.piccountbe.domain.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Purchase {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long purchaseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	@Column(nullable = false)
	private LocalDateTime purchasedAt;

	public Purchase(Member member, Item item, LocalDateTime purchasedAt) {
		this.member = member;
		this.item = item;
		this.purchasedAt = purchasedAt;
	}
}
