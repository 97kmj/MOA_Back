package com.moa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import com.moa.entity.Artwork;
import com.moa.shop.dto.ArtworkDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface ArtworkRepository extends JpaRepository<Artwork, Long> {

@Query("SELECT a FROM Artwork a " +
	"WHERE (:subject IS NULL OR a.subject.subjectName = :subject) " +
	"AND (:type IS NULL OR a.type.typeName = :type) " +
	"AND (:category IS NULL OR a.category.categoryName = :category) " +
	"AND (:search IS NULL OR LOWER(a.artist.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
	"OR LOWER(a.title) LIKE LOWER(CONCAT('%', :search, '%')))")
	Page<Artwork> findByFilters(
	@Param("subject") String subject,
	@Param("type") String type,
	@Param("category") String category,
	@Param("search") String search,
	Pageable pageable
);




}
