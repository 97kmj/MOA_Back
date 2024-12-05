package com.moa.admin.repository;

import com.moa.entity.QFundingImage;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AdminFundingRepository {
	private final JPAQueryFactory jpaQueryFactory;
	
	public List<String> getFundingImageUrlByFundingId(Long fundingId) {
		QFundingImage fundingImage = QFundingImage.fundingImage;
		return jpaQueryFactory.select(fundingImage.imageUrl)
				.from(fundingImage)
				.where(fundingImage.funding.fundingId.eq(fundingId))
				.fetch();
	}
}
