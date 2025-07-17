package com.solux.piccountbe.domain.callendar.repository;

import java.util.List;
import com.solux.piccountbe.domain.callendar.entity.CalendarEntry;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.callendar.entity.IncomeDetail;

public interface IncomeDetailRepository extends JpaRepository<IncomeDetail, Long> {
    List<IncomeDetail> findAllByCalendarEntry(CalendarEntry calendarEntry);
}
