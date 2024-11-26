package com.moa.repository;

import com.moa.entity.Artwork;
import com.moa.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.LikeArtwork;

public interface LikeArtworkRepository extends JpaRepository<LikeArtwork, Long> {
    Optional<LikeArtwork> findByUserAndArtwork(User user, Artwork artwork);

    List<LikeArtwork> findByUser(User user);
}
