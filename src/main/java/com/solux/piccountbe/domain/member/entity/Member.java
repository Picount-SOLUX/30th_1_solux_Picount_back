package com.solux.piccountbe.domain.member.entity;

import java.util.ArrayList;
import java.util.List;

import com.solux.piccountbe.domain.budget.entity.Budget;
import com.solux.piccountbe.domain.category.entity.Category;
import com.solux.piccountbe.global.Timestamped;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Member extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long memberId;

	@Column
	@Enumerated(value = EnumType.STRING)
	private Provider provider;

	@Column(nullable = false, unique = true)
	private String email;

	@Column
	private Long oauthId;

	@Column // oauth 경우 nullable
	private String password;

	@Column(nullable = false, unique = true)
	private String nickname;

	@Column
	@Enumerated(value = EnumType.STRING)
	private Occupation occupation;

	@Column
	private String purpose;

	@Column(nullable = false)
	private String profileImageUrl;

	@Column
	private String intro;

	@Column
	@Enumerated(value = EnumType.STRING)
	private Gender gender;

	@Column(nullable = false, unique = true, length = 8)
	private String friendCode;

	@Column
	private Integer age;

	@Column(nullable = false)
	private Boolean withdraw;

	@Column(nullable = false)
	private Boolean isMainVisible;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Token> tokens = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
	private List<Category> categories = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
	private List<Budget> budgets = new ArrayList<>();

	@Column(nullable = false)
	private Long point = 0L; // 기본값 0, long 타입

	@Builder
	public Member(Provider provider, String email, Long oauthId, String password, String nickname, String profileImageUrl, Gender gender, String friendCode, Integer age, Boolean withdraw,
		Boolean isMainVisible) {
		this.provider = provider;
		this.email = email;
		this.oauthId = oauthId;
		this.password = password;
		this.nickname = nickname;
		this.profileImageUrl = profileImageUrl;
		this.gender = gender;
		this.friendCode = friendCode;
		this.age = age;
		this.withdraw = withdraw;
		this.isMainVisible = isMainVisible;
	}

	public Member memberUpdate(String email) {
		this.email = email;
		return this;
	}

	// 포인트 차감 메서드
	public void usePoint(Long amount) {
		if (this.point < amount) {
			throw new IllegalArgumentException("포인트가 부족합니다.");
		}
		this.point -= amount;
	}

	// 포인트 추가 메서드
	public void addPoint(Long amount) {
		this.point += amount;
	}

}
