package com.moa.user.controller;

import com.moa.entity.Artwork;
import com.moa.user.service.LikeService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    // 좋아요 추가/삭제
    @PostMapping("/{artworkId}")
    public ResponseEntity<?> toggleLike(
        @PathVariable Long artworkId,
        @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "로그인이 필요합니다."));
        }

        String username = userDetails.getUsername(); // 현재 로그인한 사용자의 username
        boolean isLiked = likeService.toggleLike(username, artworkId);
        return ResponseEntity.ok(Map.of("isLiked", isLiked));
    }


}
