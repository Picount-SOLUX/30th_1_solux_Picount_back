package com.solux.piccountbe.domain.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import com.solux.piccountbe.domain.friend.entity.GuestBook;
import com.solux.piccountbe.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {

    Page<GuestBook> findByOwnerAndIsDeletedFalse(Member owner, Pageable pageable);
}