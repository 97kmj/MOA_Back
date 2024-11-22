package com.moa.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;

public class JwtDecoder {

    private static final String SECRET_KEY = "your-secret-key-must-be-256-bit"; // Replace with your secret key

    // 토큰에서 클레임 데이터를 추출하는 메서드
    public Claims decodeToken(String token) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody(); // 클레임 데이터를 반환
    }
}
