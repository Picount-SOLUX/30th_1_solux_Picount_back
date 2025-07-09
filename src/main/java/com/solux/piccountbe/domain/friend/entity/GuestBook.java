package com.solux.piccountbe.domain.friend.entity;

import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.global.Timestamped;

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
public class GuestBook extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long guestbookId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="writer_id", nullable=false)
	private Member writer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="owner_id", nullable=false)
	private Member owner;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private Boolean isDeleted;
}
