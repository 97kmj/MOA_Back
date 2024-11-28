package com.moa.funding.repository;

import java.time.Instant;
import java.util.List;

import com.moa.entity.FundingOrder;

public interface FundingStatusRepositoryCustom {
	void updateFundingToOnGoing();
	void updateFundingToSuccessful();

	void updateFundingToFailed();

	List<Long> updateFundingToFailedAndGetIds(Instant now);
	List<FundingOrder> findOrdersByFundingId(Long fundingId);
}
