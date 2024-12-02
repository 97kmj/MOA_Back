package com.moa.user.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.moa.entity.Funding;
import com.moa.entity.QFunding;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MainFundingRepository {
	private final JPAQueryFactory jpaQueryFactory;
	
	public List<Funding> get4FundingList() {
		QFunding funding = QFunding.funding;
		return jpaQueryFactory.selectFrom(funding)
				.where(funding.fundingStatus.eq(Funding.FundingStatus.ONGOING))
				.orderBy(funding.startDate.desc())
				.limit(4)
				.fetch();
	}
}
