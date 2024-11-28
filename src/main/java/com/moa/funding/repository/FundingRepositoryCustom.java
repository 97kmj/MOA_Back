package com.moa.funding.repository;

import org.springframework.transaction.annotation.Transactional;

import com.moa.funding.dto.funding.FundingDetailDTO;
import com.moa.funding.dto.funding.FundingResponse;

public interface FundingRepositoryCustom {
	FundingDetailDTO findFundingDetailById(Long fundingId);

	FundingResponse findFundingList(String filterType, String sortOption, int page);

    void updateFundingToOnGoing();
	void updateFundingToSuccessful();

	void updateFundingToFailed();
}
