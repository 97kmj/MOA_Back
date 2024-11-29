package com.moa.funding.repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.moa.entity.Funding;
import com.moa.entity.FundingContribution;
import com.moa.entity.FundingOrder;
import com.moa.entity.QFunding;
import com.moa.entity.QFundingContribution;
import com.moa.entity.QFundingOrder;
import com.moa.entity.QReward;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class FundingManagementRepositoryCustomImpl implements FundingManagementRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Autowired
	public FundingManagementRepositoryCustomImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}



	@Override
	public Optional<FundingOrder> findRefundableOrderById(Long fundingOrderId) {
		QFundingOrder fundingOrder = QFundingOrder.fundingOrder;
		QFunding funding = QFunding.funding;

		FundingOrder result = queryFactory
			.selectFrom(fundingOrder)
			.leftJoin(fundingOrder.funding, funding).fetchJoin()
			.where(
				fundingOrder.fundingOrderId.eq(fundingOrderId)
					.and(funding.approvalStatus.eq(Funding.ApprovalStatus.APPROVED)) // 승인된 펀딩
					.and(funding.fundingStatus.in(Funding.FundingStatus.ONGOING, Funding.FundingStatus.SUCCESSFUL)) // 진행 중 또는 성공
					.and(funding.endDate.after(Instant.now())) // 종료일이 현재 이후
					.and(fundingOrder.refundStatus.eq(FundingOrder.RefundStatus.NOT_REFUNDED)) // 환불되지 않은 상태
					.and(fundingOrder.totalAmount.gt(0)) // 환불 금액이 있는 주문
			)
			.fetchOne();

		return Optional.ofNullable(result);
	}

	@Override
	public List<FundingContribution> findContributionsByFundingOrderId(Long fundingOrderId) {
		QFundingContribution fundingContribution = QFundingContribution.fundingContribution;
		QReward reward = QReward.reward;

		return queryFactory
			.selectFrom(fundingContribution)
			.leftJoin(fundingContribution.reward, reward).fetchJoin()
			.where(fundingContribution.fundingOrder.fundingOrderId.eq(fundingOrderId))
			.fetch();
	}

	@Override
	public void updateRefundStatus(FundingOrder fundingOrder) {
		QFundingOrder fundingOrderEntity = QFundingOrder.fundingOrder;

		queryFactory.update(fundingOrderEntity)
			.set(fundingOrderEntity.refundStatus, FundingOrder.RefundStatus.REFUNDED)
			.where(fundingOrderEntity.fundingOrderId.eq(fundingOrder.getFundingOrderId()))
			.execute();

	}

	@Override
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

		if(fundingIdsNotEmpty(fundingIds)) {
			queryFactory.update(funding)
				.set(funding.fundingStatus, Funding.FundingStatus.FAILED)
				.where(funding.fundingId.in(fundingIds))
				.execute();
		}
		return fundingIds;
	}

	private static boolean fundingIdsNotEmpty(List<Long> fundingIds) {
		return !fundingIds.isEmpty();
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
