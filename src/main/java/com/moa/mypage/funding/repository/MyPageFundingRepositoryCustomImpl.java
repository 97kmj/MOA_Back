package com.moa.mypage.funding.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.moa.entity.Funding;
import com.moa.entity.FundingOrder;
import com.moa.entity.QFunding;
import com.moa.entity.QFundingOrder;
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

	private BooleanExpression getFilterCondition(String status) {
		QFunding funding = QFunding.funding;

	switch (status) {
		case "ONGOING" :
			return funding.fundingStatus.eq(Funding.FundingStatus.ONGOING);
		case "SUCCESSFUL" :
			return funding.fundingStatus.eq(Funding.FundingStatus.SUCCESSFUL);
		case "FAILED" :
			return funding.fundingStatus.eq(Funding.FundingStatus.FAILED);
		case "CANCELLED" :
			return funding.fundingStatus.eq(Funding.FundingStatus.CANCELLED);
		default:
			return null;
	}
	}




}
