package com.moa.funding.repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.moa.entity.Funding;
import com.moa.entity.FundingOrder;
import com.moa.entity.QFunding;
import com.moa.entity.QFundingOrder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.moa.entity.QFunding.funding;
import static com.moa.entity.QReward.reward;
import static com.moa.entity.QFundingImage.fundingImage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class FundingStatusRepositoryCustomImpl implements FundingStatusRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Autowired
	public FundingStatusRepositoryCustomImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}


	@Override
	@Transactional
	public void updateFundingToOnGoing() {
		QFunding funding = QFunding.funding;

		//오늘 날짜를 구함
		LocalDate today = LocalDate.now();
		//오늘 자정을 구함 2024-11-28 00:00 (한국) = 2024-11-27 15:00 (UTC)
		Instant startOfToday = today.atStartOfDay(ZoneId.systemDefault()).toInstant();

		//오늘에서 하루를 더해서 내일 자정을 구함 그리고 -1초를 해서 오늘의 마지막 시간 23:59:59를 구했당~
		Instant endOfToday = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

		long updatedCount = queryFactory.update(funding)
			.set(funding.fundingStatus, Funding.FundingStatus.ONGOING)
			.where(funding.approvalStatus.eq(Funding.ApprovalStatus.APPROVED)
					.and(funding.fundingStatus.eq(Funding.FundingStatus.STANDBY))
					.and(funding.startDate.between(startOfToday, endOfToday))
				// .and(funding.startDate.goe(startOfToday)) // startDate >= 오늘 자정
				// .and(funding.startDate.lt(endOfToday))    // startDate < 내일 자정
			)
			.execute();

		log.info("updateFundingToOnGoing updatedCount: {}", updatedCount);
	}

	@Override
	@Transactional
	public void updateFundingToSuccessful() {
		QFunding funding = QFunding.funding;

		Instant today = Instant.now();

		long updateCount = queryFactory.update(funding)
			.set(funding.fundingStatus, Funding.FundingStatus.SUCCESSFUL)
			.where (funding.approvalStatus.eq(Funding.ApprovalStatus.APPROVED)
				.and(funding.fundingStatus.eq(Funding.FundingStatus.ONGOING)
					.and(funding.currentAmount.goe(funding.goalAmount)))
				.and(funding.endDate.goe(today)) //endDate 날짜 >= 오늘  오늘보다 크거나 같은 경우
			)
			.execute();

		log.info("updateFundingToSuccessful updateCount: {}", updateCount);
	}

	@Override
	@Transactional
	public void updateFundingToFailed(){
		// QFunding funding = QFunding.funding;
		//
		// Instant today = Instant.now();
		//
		// long updatedCount = queryFactory.update(funding)
		// 	.set(funding.fundingStatus, Funding.FundingStatus.FAILED)
		// 	.where(funding.approvalStatus.eq(Funding.ApprovalStatus.APPROVED)
		// 		.and(funding.fundingStatus.eq(Funding.FundingStatus.ONGOING))
		// 		.and(funding.endDate.before(today)) //endDate 날짜 < 오늘
		// 		.and(funding.currentAmount.lt(funding.goalAmount)) // 현재금액 < 목표금액 :현재 금액이 목표금액보다 작은 경우
		// 	)
		// 	.execute();
		//
		// log.info("updateFundingToFailed updatedCount: {}", updatedCount);
	}

	@Override
	public List<Long> updateFundingToFailedAndGetIds(Instant now) {
		QFunding funding = QFunding.funding;

		// Step 1: Fetch funding IDs to update
		List<Long> fundingIds = queryFactory.select(funding.fundingId)
			.from(funding)
			.where(funding.approvalStatus.eq(Funding.ApprovalStatus.APPROVED)
				.and(funding.fundingStatus.eq(Funding.FundingStatus.ONGOING))
				.and(funding.endDate.before(now))
				.and(funding.currentAmount.lt(funding.goalAmount)))
			.fetch();

		queryFactory.update(funding)
			.set(funding.fundingStatus, Funding.FundingStatus.FAILED)
			.where(funding.fundingId.in(fundingIds))
			.execute();

		return fundingIds;
	}

	@Override
	public List<FundingOrder> findOrdersByFundingId(Long fundingId) {
		QFundingOrder fundingOrder = QFundingOrder.fundingOrder;

		return queryFactory.selectFrom(fundingOrder)
			.where(fundingOrder.funding.fundingId.eq(fundingId)
				.and(fundingOrder.refundStatus.eq(FundingOrder.RefundStatus.NOT_REFUNDED)))
			.fetch();
	}

}
