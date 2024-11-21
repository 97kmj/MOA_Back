package com.moa.config.jwt;

import org.springframework.stereotype.Component;
import java.util.Date;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component // Bean으로 등록
public class JwtToken {

    // Access Token 생성
    public String makeAccessToken(String username) {
        return JWT.create()
            .withSubject(username)
            .withIssuedAt(new Date(System.currentTimeMillis()))
            .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
            .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    // Refresh Token 생성
    public String makeRefreshToken(String username) {
        return JWT.create()
            .withSubject(username)
            .withIssuedAt(new Date(System.currentTimeMillis()))
            .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME))
            .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    // 토큰 검증 메서드 추가
    public boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰에서 사용자 이름(subject) 추출
    public String getUsernameFromToken(String token) {
        return JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
            .build()
            .verify(token)
            .getSubject();
    }
}
