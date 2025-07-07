package com.solux.piccountbe.domain.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.friend.entity.Friend;

public interface FriendRepository extends JpaRepository<Friend, Long> {
}
