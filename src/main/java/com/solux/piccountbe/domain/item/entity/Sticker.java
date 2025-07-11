package com.solux.piccountbe.domain.item.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Sticker {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long stickerId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	@Column(nullable = false)
	private String stickerUrl;

	public Sticker(Item item, String stickerUrl) {
		this.item = item;
		this.stickerUrl = stickerUrl;
	}

}
