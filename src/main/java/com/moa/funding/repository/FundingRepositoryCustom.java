package com.moa.funding.repository;

import com.moa.funding.dto.funding.FundingDetailDTO;

public interface FundingRepositoryCustom {
	FundingDetailDTO findFundingDetailById(Long fundingId);
}
