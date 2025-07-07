package com.solux.piccountbe.domain.callendar.entity;

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
public class CalendarPhoto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long photoId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="calendar_entry_id", nullable = false)
	private CalendarEntry calendarEntry;

	@Column(nullable = false)
	private String filePath;

	@Column
	private Float fileSizeMb;
}
