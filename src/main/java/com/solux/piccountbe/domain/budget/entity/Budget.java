package com.solux.piccountbe.domain.budget.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.solux.piccountbe.domain.member.entity.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Budget {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long budgetId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column
	private LocalDate startDate;

	@Column
	private LocalDate endDate;

	@Column
	private Integer totalAmount;

	@Column
	@Setter
	private Boolean active;

	@OneToMany(mappedBy = "budget", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<BudgetAllocation> budgetAllocationList = new ArrayList<>();
}
