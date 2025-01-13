package com.moa.funding.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import com.moa.entity.FundingContribution;
import com.moa.entity.FundingOrder;
import com.moa.funding.dto.funding.FundingDetailDTO;
import com.moa.funding.dto.funding.FundingResponse;

public interface FundingSelectRepositoryCustom {
	FundingDetailDTO findFundingDetailById(Long fundingId);

	Optional<FundingOrder> findFundingOrderByMerchantUid(String merchantUid);

	FundingResponse findFundingList(String filterType, String sortOption, int page);

	List<FundingContribution> findContributionsByOrderId(Long fundingOrderId);
	List<FundingOrder> findPendingOrdersOlderThan(Timestamp cutoffTime);

}
