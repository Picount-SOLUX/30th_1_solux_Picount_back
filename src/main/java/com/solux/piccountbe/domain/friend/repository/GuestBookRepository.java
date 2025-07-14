package com.solux.piccountbe.domain.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.solux.piccountbe.domain.friend.entity.GuestBook;
import com.solux.piccountbe.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;

public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {
    @Query("SELECT g FROM GuestBook g WHERE g.writer.id = :writerId AND g.isDeleted = false ORDER BY g.createdAt DESC")
    Page<GuestBook> findByWriterId(@Param("writerId") Long writerId, Pageable pageable);
    Page<GuestBook> findByOwnerAndIsDeletedFalse(Member owner, Pageable pageable);
}