package com.solux.piccountbe.domain.callendar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.solux.piccountbe.domain.callendar.entity.CalendarEntry;
import com.solux.piccountbe.domain.callendar.entity.CalendarPhoto;

public interface CalendarPhotoRepository extends JpaRepository<CalendarPhoto, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM CalendarPhoto cp WHERE cp.calendarEntry = :entry")
    void deleteByCalendarEntry(@Param("entry") CalendarEntry entry);
    List<CalendarPhoto> findAllByCalendarEntry(CalendarEntry calendarEntry);
}