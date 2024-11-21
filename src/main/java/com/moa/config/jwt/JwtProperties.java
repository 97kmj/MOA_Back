package com.moa.config.jwt;

public class JwtProperties {
    public static final String SECRET = "your_secret_key"; // 환경 변수로 대체 권장
    public static final long ACCESS_EXPIRATION_TIME = 1000L * 60 * 10; // 10분
    public static final long REFRESH_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 7; // 7일
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
