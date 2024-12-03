package com.moa.funding.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.moa.entity.FundingContribution;
import com.moa.entity.FundingOrder;

public interface FundingManagementRepositoryCustom {

	Optional<FundingOrder> findRefundableOrderById(Long fundingOrderId);

	List<FundingContribution> findContributionsByFundingOrderId(Long fundingOrderId);

	void updateRefundStatus(FundingOrder fundingOrder);

	void updateFundingToOnGoing();

	void updateFundingToSuccessful();

	List<Long> updateFundingToFailedAndGetIds(Instant now);

	void updateFundingToOngoingIfRefund();

	void validateAndUpdateFundingStatuses();

	List<FundingOrder> findOrdersByFundingId(Long fundingId);

}
