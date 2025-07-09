package com.solux.piccountbe.domain.callendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.callendar.entity.CalendarEntry;

public interface CalendarEntryRepository  extends JpaRepository<CalendarEntry, Long> {
}