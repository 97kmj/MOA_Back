package com.moa.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moa.config.auth.PrincipalDetails;
import com.moa.entity.User;
import com.moa.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;
    private final JwtToken jwtToken;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JwtToken jwtToken) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtToken = jwtToken;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        // 등록 및 로그인 관련 경로는 필터를 적용하지 않음
        return path.equals("/api/user/register") || path.equals("/auth/login") || path.startsWith("/oauth2/") || path.equals("/adminQnA");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        String uri = request.getRequestURI();

        // 인증이 필요 없는 경로는 필터를 통과시킴
        if (!requiresAuthentication(uri)) {
            chain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader(JwtProperties.HEADER_STRING);
        if (authorizationHeader == null || !authorizationHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인 필요");
            return;
        }

        try {
            String accessToken = authorizationHeader.replace(JwtProperties.TOKEN_PREFIX, "");
            String username = validateToken(accessToken);

            // 사용자 정보 조회 및 SecurityContext 설정
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            setAuthentication(user);

            chain.doFilter(request, response);

        } catch (Exception e) {
            handleRefreshToken(request, response);
        }
    }

    private boolean requiresAuthentication(String uri) {
        return uri.contains("/user") || uri.contains("/admin") || uri.contains("/artist");
    }



    private String validateToken(String token) {
        return JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
            .build()
            .verify(token)
            .getSubject(); // username 반환
    }

    private void setAuthentication(User user) {
        PrincipalDetails principalDetails = new PrincipalDetails(user);
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void handleRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String authorizationHeader = request.getHeader(JwtProperties.HEADER_STRING);
        Map<String, String> tokenMap = objectMapper.readValue(authorizationHeader, Map.class);

        String refreshToken = tokenMap.get("refresh_token");
        if (refreshToken == null || !refreshToken.startsWith(JwtProperties.TOKEN_PREFIX)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인 필요");
            return;
        }

        try {
            String username = validateToken(refreshToken.replace(JwtProperties.TOKEN_PREFIX, ""));
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            // 새로운 Access Token 및 Refresh Token 생성
            String newAccessToken = jwtToken.makeAccessToken(username);
            String newRefreshToken = jwtToken.makeRefreshToken(username);

            Map<String, String> newTokenMap = new HashMap<>();
            newTokenMap.put("access_token", JwtProperties.TOKEN_PREFIX + newAccessToken);
            newTokenMap.put("refresh_token", JwtProperties.TOKEN_PREFIX + newRefreshToken);

            response.setHeader(JwtProperties.HEADER_STRING, objectMapper.writeValueAsString(newTokenMap));
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(newTokenMap));

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰 검증 실패");
        }
    }
}
