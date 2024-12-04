package com.moa.mypage.funding.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.moa.entity.Funding;
import com.moa.entity.FundingContribution;
import com.moa.entity.FundingOrder;
import com.moa.entity.QFunding;
import com.moa.entity.QFundingContribution;
import com.moa.entity.QFundingOrder;
import com.moa.entity.QReward;
import com.moa.entity.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class MyPageFundingRepositoryCustomImpl implements MyPageFundingRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Autowired
	public MyPageFundingRepositoryCustomImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public Page<FundingOrder> findMyContributedFunding(String username, String status, Pageable pageable) {
		QFundingOrder fundingOrder = QFundingOrder.fundingOrder;
		QFunding funding = QFunding.funding;

		BooleanExpression filterCondition = getFilterCondition(status);

		List<FundingOrder> orders = queryFactory
			.selectFrom(fundingOrder)
			.join(fundingOrder.funding, funding).fetchJoin()
			.where(
				fundingOrder.user.username.eq(username)
					.and(filterCondition)
			)
			.offset(pageable.getOffset())// 페이지 시작 위치
			.limit(pageable.getPageSize())// 페이지 사이즈
			.fetch();

		// 전체 데이터수 가져오는거
		// Optional로 처리
		long total = Optional.ofNullable(queryFactory
				.select(fundingOrder.count())
				.from(fundingOrder)
				.where(
					fundingOrder.user.username.eq(username)
						.and(filterCondition)
				)
				.fetchOne())
			.orElse(0L);

		return new PageImpl<>(orders, pageable, total);

	}

	@Override
	public FundingOrder findFundingOrderWithUser(Long fundingOrderId) {
		QFundingOrder fundingOrder = QFundingOrder.fundingOrder;
		QUser user = QUser.user;

		return queryFactory.selectFrom(fundingOrder)
			.join(fundingOrder.user, user).fetchJoin()
			.where(fundingOrder.fundingOrderId.eq(fundingOrderId))
			.fetchOne();
	}

	@Override
	public List<FundingContribution> findFundingContributions(Long fundingOrderId) {
		QFundingContribution fundingContribution = QFundingContribution.fundingContribution;
		QReward reward = QReward.reward;

		return queryFactory.selectFrom(fundingContribution)
			.leftJoin(fundingContribution.reward, reward).fetchJoin()
			.where(fundingContribution.fundingOrder.fundingOrderId.eq(fundingOrderId))
			.fetch();
	}

	@Override
	public FundingOrder findFundingOrderWithFunding(Long fundingOrderId) {
		QFundingOrder fundingOrder = QFundingOrder.fundingOrder;
		QFunding funding = QFunding.funding;

		return queryFactory.selectFrom(fundingOrder)
			.leftJoin(fundingOrder.funding, funding).fetchJoin()
			.where(fundingOrder.fundingOrderId.eq(fundingOrderId))
			.fetchOne();

	}




	@Override
	public Page<Funding> findMyRegisteredFunding(String username, String status, Pageable pageable) {
		QFunding funding = QFunding.funding;

		BooleanExpression filterCondition = getFilterCondition(status);

		List<Funding> fundingList = queryFactory
			.selectFrom(funding)
			.where(funding.user.username.eq(username).and(filterCondition))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = Optional.ofNullable(queryFactory
			.select(funding.count())
			.from(funding)
			.where(funding.user.username.eq(username).and(filterCondition))
			.fetchOne()).orElse(0L);

		return new PageImpl<>(fundingList, pageable, total);

	}

	@Override
	public Page<FundingContribution> findContributionsForMyFunding(Long fundingId, Pageable pageable) {
		QFundingContribution contribution = QFundingContribution.fundingContribution;
		QFundingOrder fundingOrder = QFundingOrder.fundingOrder;
		QFunding funding = QFunding.funding;
		QUser user = QUser.user;
		QReward reward = QReward.reward;

		// 펀딩 기여 정보 조회 (FundingContribution과 관련된 Reward 포함)
		List<FundingContribution> contributions = queryFactory
			.selectFrom(contribution)
			.join(contribution.fundingOrder, fundingOrder).fetchJoin()
			.leftJoin(fundingOrder.funding, funding).fetchJoin()
			.join(fundingOrder.user, user).fetchJoin()
			.leftJoin(contribution.reward, reward)  // Reward와의 관계도 가져옴
			.where(funding.fundingId.eq(fundingId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// Total count
		long total = Optional.ofNullable(queryFactory
			.select(contribution.count())
			.from(contribution)
			.join(contribution.fundingOrder, fundingOrder)
			.join(fundingOrder.funding, funding)
			.where(fundingOrder.funding.fundingId.eq(fundingId))
			.fetchOne()).orElse(0L);

		return new PageImpl<>(contributions, pageable, total);
	}


	private BooleanExpression getFilterCondition(String status) {
		QFunding funding = QFunding.funding;

		switch (status) {
			case "ONGOING":
				return funding.fundingStatus.eq(Funding.FundingStatus.ONGOING);
			case "SUCCESSFUL":
				return funding.fundingStatus.eq(Funding.FundingStatus.SUCCESSFUL);
			case "FAILED":
				return funding.fundingStatus.eq(Funding.FundingStatus.FAILED);
			case "CANCELLED":
				return funding.fundingStatus.eq(Funding.FundingStatus.CANCELLED);
			default:
				return null;
		}
	}


	@Override
	public Page<FundingOrder> findFundingOrdersForFunding(Long fundingId, Pageable pageable) {
		QFundingOrder fundingOrder = QFundingOrder.fundingOrder;
		QFunding funding = QFunding.funding;
		QUser user = QUser.user;

		// FundingOrder 관련 정보 조회
		List<FundingOrder> fundingOrders = queryFactory
			.selectFrom(fundingOrder)
			.join(fundingOrder.funding, funding)
			.join(fundingOrder.user, user)
			.where(funding.fundingId.eq(fundingId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// Total count 조회
		long total = Optional.ofNullable(queryFactory
			.select(fundingOrder.count())
			.from(fundingOrder)
			.join(fundingOrder.funding, funding)
			.join(fundingOrder.user, user)
			.where(funding.fundingId.eq(fundingId))
			.fetchOne()).orElse(0L);

		return new PageImpl<>(fundingOrders, pageable, total);
	}


	@Override
	public List<FundingContribution> findContributionsByFundingOrderId(Long fundingOrderId) {
		QFundingContribution contribution = QFundingContribution.fundingContribution;
		QFundingOrder fundingOrder = QFundingOrder.fundingOrder;
		QReward reward = QReward.reward;

		return queryFactory
			.selectFrom(contribution)
			.join(contribution.fundingOrder, fundingOrder)
			.leftJoin(contribution.reward, reward)
			.where(fundingOrder.fundingOrderId.eq(fundingOrderId))
			.fetch();
	}







}
