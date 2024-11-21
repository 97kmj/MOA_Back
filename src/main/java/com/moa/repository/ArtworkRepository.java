package com.moa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.Artwork;
import com.moa.shop.dto.ArtworkDto;

public interface ArtworkRepository extends JpaRepository<Artwork, Long> {

	void save(ArtworkDto artworkDto);

}
