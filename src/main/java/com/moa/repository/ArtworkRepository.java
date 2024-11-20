package com.moa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.Artwork;

public interface ArtworkRepository extends JpaRepository<Artwork, Long> {

}
