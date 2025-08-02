package com.solux.piccountbe.domain.member;

import java.security.SecureRandom;

public class GenerateRandomCode {

	private static final String CODE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final int CODE_LEN = 8;
	private static final SecureRandom random = new SecureRandom();

	public static String generateRandomCode() {

		StringBuilder sb = new StringBuilder(CODE_LEN);
		for (int i = 0; i < CODE_LEN; i++) {
			sb.append(CODE_CHARS.charAt(random.nextInt(CODE_CHARS.length())));
		}
		return sb.toString();
	}
}

