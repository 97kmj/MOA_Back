package com.moa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import com.moa.entity.Artwork;
import com.moa.shop.dto.ArtworkDto;

public interface ArtworkRepository extends JpaRepository<Artwork, Long> {

//	void save(ArtworkDto artworkDto,MultipartFile artworkImage);

}
