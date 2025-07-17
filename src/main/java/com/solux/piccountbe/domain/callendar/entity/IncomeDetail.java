package com.solux.piccountbe.domain.callendar.entity;

import com.solux.piccountbe.domain.category.entity.Category;

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
public class IncomeDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long incomeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="calendar_entry_id", nullable = false)
	private CalendarEntry calendarEntry;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="category_id", nullable = false)
	private Category category;

	@Column(nullable = false)
	private Integer amount;

	public IncomeDetail(CalendarEntry calendarEntry, Category category, Integer amount) {
		this.calendarEntry = calendarEntry;
		this.category = category;
		this.amount = amount;
	}
}
