package com.moa.config.oauth;

import com.moa.config.auth.PrincipalDetails;
import com.moa.config.jwt.JwtToken;
import com.moa.config.jwt.JwtProperties;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private static final String REDIRECT_URI = "http://localhost:3000/login";
    private final JwtToken jwtToken;

    public OAuth2SuccessHandler(JwtToken jwtToken) {
        this.jwtToken = jwtToken; // JwtToken을 DI로 주입받음
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        // 인증된 사용자 정보 가져오기
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // JWT 생성
        String accessToken = jwtToken.makeAccessToken(principalDetails.getUsername());
        String refreshToken = jwtToken.makeRefreshToken(principalDetails.getUsername());

        // JSON 형식의 토큰 데이터 생성
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("access_token", JwtProperties.TOKEN_PREFIX + accessToken);
        tokenMap.put("refresh_token", JwtProperties.TOKEN_PREFIX + refreshToken);

        // JSON 직렬화
        ObjectMapper objectMapper = new ObjectMapper();
        String tokenJson = objectMapper.writeValueAsString(tokenMap);

        System.out.println("Generated Token: " + tokenJson);

        // URL 리다이렉트 생성
        String redirectUrl = UriComponentsBuilder.fromUriString(REDIRECT_URI)
            .queryParam("token", tokenJson)
            .build()
            .toUriString();

        // 클라이언트로 리다이렉트
        response.sendRedirect(redirectUrl);
    }
}
