package com.solux.piccountbe.domain.category.entity;

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
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long categoryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn (name = "member_id")
	private Member member;

	@Column
	private String name;

	@Column
	@Enumerated(value = EnumType.STRING)
	private Type type;
}
