package com.moa.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moa.entity.Order;
// import com.moa.mypage.shop.dto.OrderArtworkDto;


public interface OrderRepository extends JpaRepository<Order, Long> {
	/*
	 * @Query("SELECT new com.moa.mypage.shop.dto.OrderArtworkDto( " +
	 * "oi.artwork.id, " + // Artwork의 id "oi.artwork.price, " + // Artwork의 가격
	 * "oi.artwork.title, " + // Artwork의 제목 "oi.artwork.artist.name, " + //
	 * Artwork의 작가 이름 "oi.artwork.imageUrl, " + // Artwork의 이미지 URL
	 * "o.paymentDate, " + // Order의 결제일 "o.orderId, " + // Order의 주문 ID
	 * "oi.quantity) " // OrderItem의 수량 "FROM Order o " + // 엔티티 이름 `Order` 사용
	 * "LEFT JOIN o.orderItems oi " + // OrderItem과의 LEFT JOIN
	 * "LEFT JOIN oi.artwork a " + // Artwork와의 LEFT JOIN
	 * "WHERE o.user.username = :username " + // 사용자명으로 필터링
	 * "AND (:startDate IS NULL OR o.paymentDate >= :startDate) " + // 시작일 조건
	 * "AND (:endDate IS NULL OR o.paymentDate <= :endDate) " + // 종료일 조건
	 * "ORDER BY o.pa" + "" + "ymentDate DESC") // 결제일을 기준으로 내림차순 정렬
	 * Page<OrderArtworkDto> findOrdersWithArtworkAndPaymentDate(
	 * 
	 * @Param("username") String userName,
	 * 
	 * @Param("startDate") Date startDate,
	 * 
	 * @Param("endDate") Date endDate, Pageable pageable);
	 */

	
//	@Query("SELECT o.orderId as orderId,o.paymentDate as paynentDate,i.artwork.title as title,u.username as artistName,o.artwork.imageUrl as imageUrl,i.quantity as quantity " +
//			"FROM Order o" +
//			"LEFT JOIN OrderItem i ON i.order.orderId = o.orderId " +
//			"LEFT JOIN User u ON u.username = i.artwork.artistId " +
//			"WHERE o.user.username = :username " +
//			"AND (:startDate IS NULL OR order.paymentDate >= :startDate) " +
//			"AND (:endDate IS NULL OR o.paymentDate <= :endDate) " +
//			"ORDER BY o.paymentDate DESC")
//			Page<OrderArtworkDto> findOrdersWithArtworkAndPaymentDate(
//		        @Param("username") String username,
//		        @Param("startDate") Date startDate,  
//		        @Param("endDate") Date endDate,      
//		        Pageable pageable					
//					
//			);



}



