package com.solux.piccountbe.domain.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.friend.entity.GuestBook;

public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {
}