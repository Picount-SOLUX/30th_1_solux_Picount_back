package com.solux.piccountbe.domain.friend.entity;

import com.solux.piccountbe.domain.member.entity.Member;
import com.solux.piccountbe.global.Timestamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Friend extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long friendId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "friend_member_id", nullable = false)
	private Member friendMember;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private Status status;

	public Friend(Member member, Member friendMember, Status status) {
		this.member = member;
		this.friendMember = friendMember;
		this.status = status;
	}
}
