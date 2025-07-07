package com.solux.piccountbe.domain.item.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long itemId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private Category category;

	@Column(nullable = false)
	private Integer price;
}