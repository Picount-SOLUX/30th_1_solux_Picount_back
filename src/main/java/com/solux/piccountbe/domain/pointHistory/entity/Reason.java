package com.solux.piccountbe.domain.pointHistory.entity;

public enum Reason {
	ATTENDANCE, //출석체크
	INVITE_FRIEND,  // 친구 초대
	NO_SPENDING,  // 무지출 챌린지
	GUESTBOOK, // 방명록 작성
	ITEM_PURCHASE, //상품 구매
	BONUS, // 보너스
	ROLLBACK // 회수 (차감)
}
