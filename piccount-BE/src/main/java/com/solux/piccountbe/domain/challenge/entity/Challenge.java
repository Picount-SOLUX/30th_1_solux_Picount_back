package com.solux.piccountbe.domain.challenge.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Challenge {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long challengeId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Integer rewardPoint;

	@Column(nullable = false)
	private Type type;
}
