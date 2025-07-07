package com.solux.piccountbe.domain.member.entity;

import java.util.ArrayList;
import java.util.List;

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

	@Column
	private String oauthId;

	@Column
	private String password;

	@Column(nullable = false)
	private String nickname;

	@Column
	@Enumerated(value = EnumType.STRING)
	private Occupation occupation;

	@Column
	private String purpose;

	@Column
	private String profileImageUrl;

	@Column
	private String intro;

	@Column
	@Enumerated(value = EnumType.STRING)
	private Gender gender;

	@Column
	private String friendCode;

	@Column
	private Integer age;

	@Column(nullable = false)
	private Boolean withdraw;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Token> tokens = new ArrayList<>();

}
