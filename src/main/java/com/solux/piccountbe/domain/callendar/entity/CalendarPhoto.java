package com.solux.piccountbe.domain.callendar.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class CalendarPhoto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long photoId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="calendar_entry_id", nullable = false)
	private CalendarEntry calendarEntry;

	@Column(nullable = false)
	private String url;

	@Column
	private Float fileSizeMb;

	public CalendarPhoto(CalendarEntry calendarEntry, String url, float fileSizeMb) {
		this.calendarEntry = calendarEntry;
		this.url = url;
		this.fileSizeMb = fileSizeMb;
	}
}
