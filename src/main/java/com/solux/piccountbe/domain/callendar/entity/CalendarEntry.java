package com.solux.piccountbe.domain.callendar.entity;

import java.time.LocalDate;

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
public class CalendarEntry extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long calenderEntryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="memberId", nullable = false)
	private Member member;

	@Column(nullable = false)
	private LocalDate entryDate;

	@Column
	private String emotionSticker;

	@Column
	private String memo;
}
