package com.moa.funding.repository;

import com.moa.funding.dto.funding.FundingDetailDTO;
import com.moa.funding.dto.funding.FundingResponse;

public interface FundingRepositoryCustom {
	FundingDetailDTO findFundingDetailById(Long fundingId);

	FundingResponse findFundingList(String filterType, String sortOption, int page);
}
