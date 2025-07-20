package com.solux.piccountbe.domain.challenge.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Type type;

	public Challenge(String name, Integer rewardPoint, Type type) {
		this.name = name;
		this.rewardPoint = rewardPoint;
		this.type = type;
	}

}
