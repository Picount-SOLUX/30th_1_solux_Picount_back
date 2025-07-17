package com.solux.piccountbe.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	USER_NICKNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자 아이디입니다."),
	USER_EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 이메일을 찾을 수 없습니다."),
	USER_DELETED(HttpStatus.FORBIDDEN, "탈퇴한 회원입니다."),
	EMAIL_DUPLICATED(HttpStatus.CONFLICT, "중복된 이메일입니다"),
	NICKNAME_DUPLICATED(HttpStatus.CONFLICT, "중복된 닉네임입니다"),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "일치하지 않는 패스워드입니다."),
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "사용자 인증에 실패했습니다."),
	MEMBER_OAUTH_MISMATCH(HttpStatus.BAD_REQUEST, "소셜 계정으로 가입된 회원은 이메일 로그인 방식을 사용할 수 없습니다."),
	MEMBER_IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "프로필 이미지 저장에 실패했습니다."),
	MEMBER_IMAGE_DIRECTORY_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "프로질 이미지 디렉토리 생성에 실패했습니다."),

	// 카테고리
	CATAEGORY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 카테고리입니다."),
	CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
	CATEGORY_NOT_MATCH_MEMBER(HttpStatus.BAD_REQUEST, "사용자의 카테고리가 아닙니다."),

	// 예산
	BUDGET_NOT_FOUND(HttpStatus.NOT_FOUND, "예산을 찾을 수 없습니다."),
	BUDGET_NOT_MATCH_MEMBER(HttpStatus.BAD_REQUEST, "사용자의 예산이 아닙니다."),

	// 상점
	NO_ITEMS_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
	STICKER_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지를 찾을 수 없습니다."),
	ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
	NOT_ENOUGH_POINT(HttpStatus.BAD_REQUEST, "포인트가 부족합니다."),

	//Token
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
	TOKEN_EXPIRATION(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
	NOT_SUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다."),
	FALSE_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰입니다."),
	HEADER_NOT_FOUND(HttpStatus.BAD_REQUEST, "헤더가 잘못되었거나 누락되었습니다."),

	// 캘린더
	CALENDAR_ENTRY_ALREADY_EXISTS(HttpStatus.CONFLICT, "해당 날짜에는 이미 작성한 기록이 존재합니다."),
	CALENDAR_EMPTY_INCOME_AND_EXPENSE(HttpStatus.BAD_REQUEST, "수입 또는 지출 중 하나는 반드시 입력해야 합니다."),
	CALENDAR_INVALID_AMOUNT(HttpStatus.BAD_REQUEST, "금액은 0보다 커야 합니다."),
	CALENDAR_TOO_MANY_PHOTOS(HttpStatus.BAD_REQUEST, "사진은 최대 3장까지 등록 가능합니다."),
	CALENDAR_PHOTO_TOO_LARGE(HttpStatus.BAD_REQUEST, "사진 용량은 5MB 이하로만 등록 가능합니다."),
	CALENDAR_FILE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
	CALENDAR_FILE_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장 중 오류가 발생했습니다.");
	private final HttpStatus status;
	private final String message;
}
