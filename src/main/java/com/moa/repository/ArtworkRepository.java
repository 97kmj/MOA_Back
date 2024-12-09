package com.moa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import com.moa.entity.Artwork;
import com.moa.entity.Artwork.SaleStatus;
import com.moa.entity.Order;
import com.moa.mypage.shop.dto.SaleArtworkDto;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long> {

	@Query("SELECT a FROM Artwork a " +
		"WHERE (:subject IS NULL OR a.subject.subjectName = :subject) " +
		"AND (:type IS NULL OR a.type.typeName = :type) " +
		"AND (:category IS NULL OR a.category.categoryName = :category) " +
		"AND (:search IS NULL OR LOWER(a.artist.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
		"OR LOWER(a.title) LIKE LOWER(CONCAT('%', :search, '%'))) " +
		"AND a.saleStatus = :saleStatus")
	Page<Artwork> findByFilters(
		@Param("subject") String subject,
		@Param("type") String type,
		@Param("category") String category,
		@Param("search") String search,
		@Param("saleStatus") Artwork.SaleStatus saleStatus, // Enum 타입으로 처리
		Pageable pageable
	);

	@Query("SELECT a FROM Artwork a WHERE a.artist.username = :username")
	List<Artwork> findByArtistUsername(@Param("username") String username);
	
	
	@Query("SELECT a FROM Artwork a " +
			"WHERE (:subject IS NULL OR a.subject.subjectName = :subject) " +
			"AND (:type IS NULL OR a.type.typeName = :type) " +
			"AND (:category IS NULL OR a.category.categoryName = :category) " +
			"AND (:status IS NULL AND a.saleStatus in ('AVAILABLE','SOLD_OUT') OR a.saleStatus = :status) " +
			"AND (:search IS NULL OR LOWER(a.artist.name) LIKE LOWER(CONCAT('%', :search, '%')))" +
			"ORDER BY a.createAt DESC")	
			Page<Artwork> findBySearches(
			@Param("subject") String subject,
			@Param("type") String type,
			@Param("category") String category,
			@Param("search") String search,
			@Param("status") SaleStatus status,
			Pageable pageable
	);
	
	
	
	
	@Query("SELECT a FROM Artwork a " +
		   "WHERE  a.saleStatus in ('AVAILABLE','SOLD_OUT')"+
		   "ORDER BY a.createAt DESC")	
	Page<Artwork>findBySaleStatus(Pageable pageable
			
			
			);
	
		
	
	// 내작품 가져오기 - 판매상태별
	 @Query("SELECT a FROM Artwork a " +
	           "WHERE (a.saleStatus = :saleStatus) " +
	           "AND (a.artist.username = :username) " +
	           "ORDER BY a.createAt DESC")
	    Page<Artwork> searchByUsernameAndSaleStatus(
	            @Param("username") String username,
	            @Param("saleStatus") SaleStatus saleStatus,
	            Pageable pageable);
	 
	// 내작품 가져오기 - 전체보기
	 @Query("SELECT a FROM Artwork a " +
	           "WHERE (:username IS NULL OR a.artist.username = :username) " +
	           "AND a.saleStatus in ('AVAILABLE','SOLD_OUT') " +
	           "ORDER BY a.createAt DESC")
	    Page<Artwork> findByUsername(
	            @Param("username") String username,
	            Pageable pageable);
					
	
	//의심작품 가져오기
	List<Artwork> findByAdminCheck(Boolean isChecked);
	
}




