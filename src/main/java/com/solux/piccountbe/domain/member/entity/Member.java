package com.solux.piccountbe.domain.member.entity;

import com.solux.piccountbe.domain.budget.entity.Budget;
import com.solux.piccountbe.domain.category.entity.Category;
import com.solux.piccountbe.global.Timestamped;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false)
//	동명이인 고려 - unique 제약조건 삭제
    private String nickname;

    @Column
    @Enumerated(value = EnumType.STRING)
    private MemberGroupType memberGroupType;

    @Column(nullable = false)
    private String profileImageUrl;

    @Column
    private String intro;

    @Column(nullable = false, unique = true, length = 8)
    private String friendCode;

    @Column(nullable = false)
    private Integer tokenVersion;

    @Column(nullable = false)
    private Boolean withdraw;

    @Column(nullable = false)
    private Boolean isMainVisible;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Budget> budgets = new ArrayList<>();

    @Column(nullable = false)
    private Long point = 0L; // 기본값 0, long 타입

    @Builder
    public Member(Provider provider, String email, Long oauthId, String password, String nickname, String profileImageUrl, String friendCode, Integer tokenVersion, Boolean withdraw,
                  Boolean isMainVisible, MemberGroupType memberGroupType) {
        this.provider = provider;
        this.email = email;
        this.oauthId = oauthId;
        this.password = password;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.friendCode = friendCode;
        this.tokenVersion = tokenVersion;
        this.withdraw = withdraw;
        this.isMainVisible = isMainVisible;
        this.memberGroupType = memberGroupType;
    }

    public Member memberEmailUpdate(String email) {
        this.email = email;
        return this;
    }

    @Transactional
    public void memberProfileUpdate(String nickname, String intro) {
        this.nickname = nickname;
        this.intro = intro;
    }

    public void memberProfileImageUrlUpdate(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void memberGroupTypeUpdate(MemberGroupType memberGroupType) {
        this.memberGroupType = memberGroupType;
    }

    public void memberPasswordUpdate(String password) {
        this.password = password;
    }

    public void plusTokenVersion() {
        this.tokenVersion++;
    }

    public void withdraw() {
        this.withdraw = true;
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

    // 공개 여부 설정
    public void updateMainVisible(Boolean isMainVisible) {
        this.isMainVisible = isMainVisible;
    }
}
