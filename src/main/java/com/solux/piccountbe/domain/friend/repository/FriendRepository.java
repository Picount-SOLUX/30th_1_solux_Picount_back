package com.solux.piccountbe.domain.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solux.piccountbe.domain.friend.entity.Friend;
import com.solux.piccountbe.domain.friend.entity.Status;
import com.solux.piccountbe.domain.member.entity.Member;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByMemberOrFriendMemberAndStatus(Member member, Member friendMember, Status status);

    boolean existsByMemberAndFriendMemberAndStatus(Member member, Member friendMember, Status status);

}
