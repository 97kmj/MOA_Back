package com.moa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.LikeArtist;

public interface LikeArtistRepository extends JpaRepository<LikeArtist, Long> {
	Optional<LikeArtist> findByArtist_UsernameAndUser_Username(String artistId,String username);
}
