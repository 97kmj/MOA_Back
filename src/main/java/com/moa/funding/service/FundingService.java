package com.moa.funding.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.moa.funding.dto.funding.ArtworkDTO;
import com.moa.funding.dto.funding.FundingDetailDTO;
import com.moa.funding.dto.funding.FundingInfoDTO;
import com.moa.funding.dto.funding.FundingResponse;
import com.moa.funding.dto.funding.RewardDTO;

public interface FundingService {

	void createFunding(FundingInfoDTO fundingInfoDTO, List<RewardDTO> rewards, List<ArtworkDTO> artwork,
		MultipartFile mainImage, List<MultipartFile> artworkImages);

		FundingDetailDTO getFundingDetail(Long fundingId);

		FundingResponse getFundingList(String filterType, String sortOption, int page);
}
