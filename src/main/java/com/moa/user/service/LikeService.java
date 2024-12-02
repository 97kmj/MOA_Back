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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    // 사용자가 특정 작품을 좋아요했는지 확인
    public boolean isLikedByUser(String username, Long artworkId) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        Artwork artwork = artworkRepository.findById(artworkId)
            .orElseThrow(() -> new IllegalArgumentException("작품을 찾을 수 없습니다."));
        System.out.println("서비스");
        return likeArtworkRepository.findByUserAndArtwork(user, artwork).isPresent();
    }

    // 사용자가 좋아요한 작품 목록 가져오기 (필터 및 페이징 적용)
    public List<Artwork> getLikedArtworks(String username, String subject, String type,
        String category, String search, int page, int size) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 페이징 처리
        PageRequest pageRequest = PageRequest.of(page, size);

        // 필터링 조건 적용
        if (subject != null || type != null || category != null || search != null) {
            return likeArtworkRepository.findLikedArtworksWithFilters(user, subject, type, category, search, pageRequest)
                .getContent();
        } else {
            return likeArtworkRepository.findByUser(user, pageRequest).getContent();
        }
    }
}