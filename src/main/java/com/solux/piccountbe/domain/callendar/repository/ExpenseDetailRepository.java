package com.solux.piccountbe.domain.callendar.repository;

import java.util.List;
import com.solux.piccountbe.domain.callendar.entity.CalendarEntry;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.callendar.entity.ExpenseDetail;

public interface ExpenseDetailRepository extends JpaRepository<ExpenseDetail, Long> {
    List<ExpenseDetail> findAllByCalendarEntry(CalendarEntry calendarEntry);
}