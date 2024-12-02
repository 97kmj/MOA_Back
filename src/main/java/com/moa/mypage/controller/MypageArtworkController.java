package com.moa.mypage.controller;

import com.moa.config.jwt.JwtToken;
import com.moa.mypage.dto.ArtworkDTO;
import com.moa.mypage.service.ArtworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/artworks")
@RequiredArgsConstructor
public class MypageArtworkController {

    private final ArtworkService artworkService;

    private final JwtToken jwtToken;

    @GetMapping("/list")
    public List<ArtworkDTO> getArtworksByArtist(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        // 헤더에서 Access Token 추출
        String accessToken = authorizationHeader.replace("Bearer ", "");

        // 토큰에서 사용자 이름 추출
        String username = jwtToken.getUsernameFromToken(accessToken);

        // 서비스 호출
        return artworkService.getArtworksByArtist(username);
    }

}