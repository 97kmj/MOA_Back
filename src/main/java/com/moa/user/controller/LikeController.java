package com.moa.user.controller;

import com.moa.config.jwt.JwtToken;
import com.moa.entity.Artwork;
import com.moa.user.service.LikeService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private JwtToken jwtToken; // JWT 토큰 유효성 검사 및 사용자 정보 추출

    // 좋아요 추가/삭제
    @PostMapping("/{artworkId}")
    public ResponseEntity<?> toggleLike(@PathVariable Long artworkId, HttpServletRequest request) {
        String username = getUsernameFromToken(request);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "로그인이 필요합니다."));
        }

        boolean isLiked = likeService.toggleLike(username, artworkId);
        return ResponseEntity.ok(Map.of("isLiked", isLiked));
    }

    // 좋아요 상태 확인
    @GetMapping("/{artworkId}")
    public ResponseEntity<?> checkLikeStatus(@PathVariable Long artworkId, HttpServletRequest request) {
        String username = getUsernameFromToken(request);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("isLiked", false));
        }

        boolean isLiked = likeService.isLikedByUser(username, artworkId);
        return ResponseEntity.ok(Map.of("isLiked", isLiked));
    }

    // 로그인한 사용자가 좋아요한 모든 작품 가져오기
    @GetMapping("/artworks")
    public ResponseEntity<List<Artwork>> getLikedArtworks(
        HttpServletRequest request,
        @RequestParam(required = false) String subject,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "8") int size) {

        String username = getUsernameFromToken(request);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Artwork> likedArtworks = likeService.getLikedArtworks(username, subject, type, category, search, page, size);
        return ResponseEntity.ok(likedArtworks);
    }

    // JWT 토큰에서 사용자 이름 추출
    private String getUsernameFromToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null; // 토큰이 없는 경우
        }

        String token = authorizationHeader.replace("Bearer ", "");
        if (!jwtToken.validateToken(token)) {
            return null; // 토큰이 유효하지 않은 경우
        }

        return jwtToken.getUsernameFromToken(token); // 토큰에서 username 추출
    }
}

