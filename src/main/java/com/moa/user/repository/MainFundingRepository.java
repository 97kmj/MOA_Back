package com.moa.user.repository;

import com.moa.entity.QFunding;
import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.moa.entity.Funding;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MainFundingRepository {
	private final JPAQueryFactory jpaQueryFactory;
	
	public List<Funding> get4FundingList() {
		QFunding funding = QFunding.funding;
		Instant today = Instant.now();
		return jpaQueryFactory.selectFrom(funding)
				.where(funding.approvalStatus.eq(Funding.ApprovalStatus.APPROVED)
						.and(
								funding.fundingStatus.eq(Funding.FundingStatus.ONGOING)
									.or(
										funding.fundingStatus.eq(Funding.FundingStatus.SUCCESSFUL)
											.and(funding.endDate.goe(today)) // 성공 펀딩 중 마감 기간 남음
									)
							))
				.orderBy(funding.startDate.desc())
				.limit(4)
				.fetch();
	}
}
