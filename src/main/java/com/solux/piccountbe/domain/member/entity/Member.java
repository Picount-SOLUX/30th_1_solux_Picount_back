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
	private String oauthId;

	@Column(nullable = false)
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

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Token> tokens = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
	private List<Category> categories = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
	private List<Budget> budgets = new ArrayList<>();

	@Builder
	public Member(Provider provider, String email, String password, String nickname, String profileImageUrl, Gender gender, String friendCode, Integer age, Boolean withdraw){
		this.provider = provider;
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.profileImageUrl = profileImageUrl;
		this.gender = gender;
		this.friendCode = friendCode;
		this.age = age;
		this.withdraw = withdraw;
	}

}
