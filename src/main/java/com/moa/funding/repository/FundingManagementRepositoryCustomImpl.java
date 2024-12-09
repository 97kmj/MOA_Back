package com.moa.funding.repository;

import com.moa.entity.QFunding;
import com.moa.entity.QFundingContribution;
import com.moa.entity.QFundingOrder;
import com.moa.entity.QReward;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.moa.entity.Funding;
import com.moa.entity.FundingContribution;
import com.moa.entity.FundingOrder;
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
					// .and(funding.endDate.after(Instant.now())) // 종료일이 오늘이후
					.and(funding.endDate.goe(Instant.now().truncatedTo(ChronoUnit.DAYS)))
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

		// 오늘에서 하루를 더한 내일 자정에서 -1초를 계산해 오늘의 마지막 순간(23:59:59)을 구함
		Instant endOfToday = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

		// 조회 없이 조건에 맞는 데이터를 업데이트 (JPA 영속성 컨텍스트에는 반영되지 않음)
		long updatedCount = queryFactory.update(funding)
			.set(funding.fundingStatus, Funding.FundingStatus.ONGOING)
			.where(funding.approvalStatus.eq(Funding.ApprovalStatus.APPROVED)
					.and(funding.fundingStatus.eq(Funding.FundingStatus.STANDBY))
					.and(funding.startDate.between(startOfToday, endOfToday))
				// .and(funding.startDate.goe(startOfToday)) // startDate >= 오늘 자정
				// .and(funding.startDate.lt(endOfToday))    // startDate < 내일 자정
			)
			.execute();
		// 이미 업데이트해야 할 조건을 명확히 알고 있기 때문에, 개별적으로 데이터를 조회한 뒤 수정할 필요 없이 바로 업데이트 쿼리를 실행

		log.debug("updateFundingToOnGoing 메서드 updatedCount: {}", updatedCount);
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
				// .and(funding.endDate.goe(today)) //endDate 날짜 >= 오늘  오늘보다 크거나 같은 경우
					.and(funding.endDate.lt(today)) // endDate의 일수가 오늘보다 작을때

			)
			.execute();

		log.debug("updateFundingToSuccessful 메서드 updateCount: {}", updateCount);
	}


	@Override
	public List<Long> updateFundingToFailedAndGetIds(Instant now) {
		QFunding funding = QFunding.funding;

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

	@Override
	public void updateFundingToOngoingIfRefund() {
		QFunding funding = QFunding.funding;

		Instant now = Instant.now();

		long updateCount = queryFactory.update(funding)
			.set(funding.fundingStatus, Funding.FundingStatus.ONGOING)
			.where(
				funding.fundingStatus.eq(Funding.FundingStatus.SUCCESSFUL) // 현재 SUCCESSFUL 상태
					.and(funding.endDate.goe(now)) // 종료일이 지나지 않은 경우
					.and(funding.currentAmount.lt(funding.goalAmount)) // 목표 금액보다 적은 경우
			)
			.execute();

		log.debug("updateFundingToOngoingIfRefund 메서드 updateCount: {}", updateCount);
	}


	@Override
	public void validateAndUpdateFundingStatuses() {
	   QFunding funding = QFunding.funding;
	   Instant now = Instant.now();

	   // ONGOING → SUCCESSFUL
		long ongoingToSuccessful = queryFactory.update(funding)
			.set(funding.fundingStatus, Funding.FundingStatus.SUCCESSFUL)
			.where(
				funding.fundingStatus.eq(Funding.FundingStatus.ONGOING)
					.and(funding.currentAmount.goe(funding.goalAmount))
					// .and(funding.endDate.goe(now))
					.and(funding.endDate.lt(now))// endDate가 현재 시간(now)보다 이전일 때 상태 변경 (endDate(1) < now(2))
			)
			.execute();
		log.debug("ONGOING → SUCCESSFUL 변경된 펀딩 수: {}", ongoingToSuccessful);

		// SUCCESSFUL → ONGOING

		long successfulToOngoing = queryFactory.update(funding)
			.set(funding.fundingStatus, Funding.FundingStatus.ONGOING)
			.where(
				funding.fundingStatus.eq(Funding.FundingStatus.SUCCESSFUL)
					// .and(funding.currentAmount.lt(funding.goalAmount))
					.and(funding.currentAmount.goe(funding.goalAmount))
					.and(funding.endDate.goe(now))// endDate가 현재 시간(now)과 같거나 이후일 때 상태 변경 (endDate(1) >= now(1))

			)
			.execute();
		log.debug("SUCCESSFUL → ONGOING 변경된 펀딩 수: {}", successfulToOngoing);

		// ONGOING → FAILED
		long ongoingToFailed = queryFactory.update(funding)
			.set(funding.fundingStatus, Funding.FundingStatus.FAILED)
		.where(
			funding.fundingStatus.eq(Funding.FundingStatus.ONGOING)
				.and(funding.currentAmount.lt(funding.goalAmount))
				.and(funding.endDate.lt(now))
		)
			.execute();
		log.debug("ONGOING → FAILED 변경된 펀딩 수: {}", ongoingToFailed);

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
