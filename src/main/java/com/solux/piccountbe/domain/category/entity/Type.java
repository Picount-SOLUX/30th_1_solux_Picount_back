package com.solux.piccountbe.domain.category.entity;

public enum Type {
	EXPENSE("지출"),
	INCOME("수입");

	private final String label;

	Type(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
