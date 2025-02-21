package com.moa.config.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moa.config.auth.PrincipalDetails;
import com.moa.entity.User;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtToken jwtToken;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtToken jwtToken) {
        this.jwtToken = jwtToken;
        // 수동으로 AuthenticationManager 설정
        setAuthenticationManager(authenticationManager);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain, Authentication authResult)
        throws IOException, ServletException {
        // PrincipalDetails로부터 사용자 정보 가져오기
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // JWT 생성
        String accessToken = jwtToken.makeAccessToken(principalDetails.getUsername());
        String refreshToken = jwtToken.makeRefreshToken(principalDetails.getUsername());

        // JSON 형식으로 토큰 데이터 생성
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("access_token", JwtProperties.TOKEN_PREFIX + accessToken);
        tokenMap.put("refresh_token", JwtProperties.TOKEN_PREFIX + refreshToken);

        // JSON 직렬화 및 응답
        ObjectMapper tokenMapper = new ObjectMapper();
        String tokenJson = tokenMapper.writeValueAsString(tokenMap);

        response.addHeader(JwtProperties.HEADER_STRING, tokenJson);
        response.setContentType("application/json; charset=utf-8");
        User user = principalDetails.getUser();
        Map<String, String> userMap = new HashMap<>();
        userMap.put("username", user.getUsername());
        userMap.put("nickname", user.getNickname());
        userMap.put("name", user.getName());
        userMap.put("email", user.getEmail());
        userMap.put("address", user.getAddress());
        userMap.put("role", user.getRole().toString());
        userMap.put("phone", user.getPhone());
        userMap.put("artistApprovalStatus", user.getArtistApprovalStatus().toString());
        ObjectMapper userMapper = new ObjectMapper();
        String userJson = userMapper.writeValueAsString(userMap);
        response.getWriter().write (userJson);
    }
}

