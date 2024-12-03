package com.moa.repository;

import com.moa.entity.Artwork;
import com.moa.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.LikeArtwork;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeArtworkRepository extends JpaRepository<LikeArtwork, Long> {

    Optional<LikeArtwork> findByUserAndArtwork(User user, Artwork artwork);

    List<LikeArtwork> findByUser(User user);

    // 사용자 기반 좋아요한 작품 목록 조회 (페이징 적용)
    @Query("SELECT la.artwork FROM LikeArtwork la WHERE la.user = :user")
    Page<Artwork> findByUser(@Param("user") User user, Pageable pageable);

    // 사용자와 필터 조건에 따라 좋아요한 작품 조회 (필터링 및 페이징)
    @Query("SELECT la.artwork FROM LikeArtwork la " +
        "WHERE la.user = :user " +
        "AND (:subject IS NULL OR la.artwork.subject.subjectName = :subject) " +
        "AND (:type IS NULL OR la.artwork.type.typeName = :type) " +
        "AND (:category IS NULL OR la.artwork.category.categoryName = :category) " +
        "AND (:search IS NULL OR LOWER(la.artwork.artist.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
        "OR LOWER(la.artwork.title) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Artwork> findLikedArtworksWithFilters(
        @Param("user") User user,
        @Param("subject") String subject,
        @Param("type") String type,
        @Param("category") String category,
        @Param("search") String search,
        Pageable pageable
    );
    
    Optional<LikeArtwork> findByUser_UsernameAndArtwork_ArtworkId(String username, Long artworkId);
    
    //좋아요 한 ID 목록 가져오기 
    @Query("SELECT l.artwork.artworkId FROM LikeArtwork l WHERE l.user.username = :username")
    List<Long> findArtworkIdsByUsername(@Param("username") String username);
}
