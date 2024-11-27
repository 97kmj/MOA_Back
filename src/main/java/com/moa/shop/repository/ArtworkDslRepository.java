package com.moa.shop.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.moa.entity.Artwork;
import com.moa.entity.QArtwork;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;





@Repository
public class ArtworkDslRepository {
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	//전체 검색
	public List<Artwork> findArtworkListByPaging(PageRequest pageRequest){
		QArtwork arkwork = QArtwork.artwork;
		return jpaQueryFactory.selectFrom(arkwork)
				.orderBy(arkwork.artworkId.desc())
				.limit(pageRequest.getPageSize())
				.fetch();		
	}
	
	
	 public List<Artwork> searchArtworkListByPaging(
	            String category,
	            String type,
	            String subject,
	            String saleStatus,
	            String keyword,
	            PageRequest pageRequest
	    ) {
	        QArtwork artwork = QArtwork.artwork;
	        // 동적 where 조건을 위한 BooleanBuilder
	        BooleanBuilder builder = new BooleanBuilder();
	        // 1. Category 조건
	        if (category != null && !category.isEmpty()) {
	            builder.and(artwork.category.categoryName.eq(category));
	        }
	        // 2. Type 조건
	        if (type != null && !type.isEmpty()) {
	            builder.and(artwork.type.typeName.eq(type));
	        }
	        // 3. Subject 조건
	        if (subject != null && !subject.isEmpty()) {
	            builder.and(artwork.subject.subjectName.eq(subject));
	        }
	        // 4. SaleStatus 조건 (열거형 처리)
	        if (saleStatus != null && !saleStatus.isEmpty()) {
	            try {
	                Artwork.SaleStatus statusEnum = Artwork.SaleStatus.valueOf(saleStatus);
	                builder.and(artwork.saleStatus.eq(statusEnum));
	            } catch (IllegalArgumentException e) {
	                // 잘못된 상태 값 처리
	                System.out.println("Invalid sale status: " + saleStatus);
	            }
	        }
	        // 5. Keyword 검색 (제목과 설명)
	        if (keyword != null && !keyword.isEmpty()) {
	            builder.and(artwork.title.containsIgnoreCase(keyword)
	                    .or(artwork.description.containsIgnoreCase(keyword)));
	        }
	        // Query 실행
	        List<Artwork> artworkList = jpaQueryFactory.selectFrom(artwork)
	                .where(builder)
	                .offset(pageRequest.getOffset())
	                .limit(pageRequest.getPageSize())
	                .orderBy(artwork.artworkId.desc()) // ID 기준 최신순 정렬
	                .fetch();

	        return artworkList;
	    }
	}