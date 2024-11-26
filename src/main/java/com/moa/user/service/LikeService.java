package com.moa.user.service;

import com.moa.entity.Artwork;
import com.moa.entity.LikeArtwork;
import com.moa.entity.User;
import com.moa.repository.ArtworkRepository;
import com.moa.repository.LikeArtworkRepository;
import com.moa.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private LikeArtworkRepository likeArtworkRepository;

    @Autowired
    private ArtworkRepository artworkRepository;

    @Autowired
    private UserRepository userRepository;

    // 좋아요 토글
    public boolean toggleLike(String username, Long artworkId) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        Artwork artwork = artworkRepository.findById(artworkId)
            .orElseThrow(() -> new IllegalArgumentException("작품을 찾을 수 없습니다."));

        // 이미 좋아요한 상태인지 확인
        Optional<LikeArtwork> like = likeArtworkRepository.findByUserAndArtwork(user, artwork);

        if (like.isPresent()) {
            likeArtworkRepository.delete(like.get());
            artwork.setLikeCount(artwork.getLikeCount() - 1); // 좋아요 감소
            artworkRepository.save(artwork);
            return false;
        } else {
            LikeArtwork newLike = LikeArtwork.builder()
                .user(user)
                .artwork(artwork)
                .build();
            likeArtworkRepository.save(newLike);
            artwork.setLikeCount(artwork.getLikeCount() + 1); // 좋아요 증가
            artworkRepository.save(artwork);
            return true;
        }
    }

    // 사용자가 좋아요한 작품 목록 가져오기
    public List<Artwork> getLikedArtworks(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return likeArtworkRepository.findByUser(user).stream()
            .map(LikeArtwork::getArtwork)
            .collect(Collectors.toList());
    }
}
