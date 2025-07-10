package com.solux.piccountbe.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NICKNAME_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 사용자 아이디입니다."),
    USER_EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 이메일을 찾을 수 없습니다."),
    USER_DELETED(HttpStatus.FORBIDDEN, "탈퇴한 회원입니다."),
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "중복된 이메일입니다"),
    NICKNAME_DUPLICATED(HttpStatus.CONFLICT, "중복된 닉네임입니다"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "일치하지 않는 패스워드입니다."),

    //Token
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
    TOKEN_EXPIRATION(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    NOT_SUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다."),
    FALSE_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰입니다."),
    HEADER_NOT_FOUND(HttpStatus.BAD_REQUEST, "헤더가 잘못되었거나 누락되었습니다.");

    private final HttpStatus status;
    private final String message;
}
