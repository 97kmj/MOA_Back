package com.moa.mypage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moa.config.jwt.JwtToken;
import com.moa.mypage.dto.ArtworkDTO;
import com.moa.mypage.service.ArtworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/artworks")
@RequiredArgsConstructor
public class MypageArtworkController {

    private final ArtworkService artworkService;
    private final JwtToken jwtToken;

    @GetMapping("/list")
    public List<ArtworkDTO> getArtworksByArtist(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String accessToken;
        try {
            System.out.println("Authorization Header: " + authorizationHeader);

            // JSON 형식인지 확인
            if (authorizationHeader.trim().startsWith("{")) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> tokenMap = objectMapper.readValue(authorizationHeader, Map.class);
                accessToken = tokenMap.get("access_token"); // JSON에서 access_token 추출
            } else if (authorizationHeader.startsWith("Bearer ")) {
                accessToken = authorizationHeader.replace("Bearer ", ""); // Bearer 제거
            } else {
                throw new RuntimeException("Invalid Authorization header format");
            }

            // Bearer 접두사가 포함된 경우 제거
            if (accessToken.startsWith("Bearer ")) {
                accessToken = accessToken.replace("Bearer ", "");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Authorization header", e);
        }

        System.out.println("Extracted Access Token: " + accessToken);

        // JWT 토큰에서 사용자 이름 추출
        if (!jwtToken.validateToken(accessToken)) {
            throw new RuntimeException("Invalid JWT Token");
        }

        String username = jwtToken.getUsernameFromToken(accessToken);

        // 서비스 호출
        return artworkService.getArtworksByArtist(username);
    }
}