package com.solux.piccountbe.domain.callendar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.callendar.entity.CalendarEntry;
import com.solux.piccountbe.domain.callendar.entity.CalendarPhoto;

public interface CalendarPhotoRepository extends JpaRepository<CalendarPhoto, Long> {
    void deleteByCalendarEntry(CalendarEntry calendarEntry);
    List<CalendarPhoto> findAllByCalendarEntry(CalendarEntry calendarEntry);
}