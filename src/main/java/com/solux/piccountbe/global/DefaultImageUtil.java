package com.solux.piccountbe.global;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultImageUtil {

    @Value("${cloud.aws.s3.base-url}")
    private String baseUrl;

    // 기본 프로필 이미지 URL 반환
    public String getDefaultProfileImageUrl() {
        return baseUrl + "/default_profile.png";
    }
}