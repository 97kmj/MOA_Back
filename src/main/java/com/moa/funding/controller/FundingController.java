package com.moa.funding.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.moa.entity.Funding;
import com.moa.funding.dto.funding.ArtworkDTO;
import com.moa.funding.dto.funding.FundingDetailDTO;
import com.moa.funding.dto.funding.FundingInfoDTO;
import com.moa.funding.dto.funding.FundingResponse;
import com.moa.funding.dto.funding.RewardDTO;
import com.moa.funding.service.FundingService;
import com.moa.repository.FundingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/funding")
public class FundingController {
	private final FundingService fundingService;

	@GetMapping
	public ResponseEntity<FundingResponse> getFundingList(
		@RequestParam String filterType,
		@RequestParam String sortOption,
		@RequestParam int page) {
		FundingResponse fundingList = fundingService.getFundingList(filterType, sortOption, page);
		return ResponseEntity.ok(fundingList);
	}



	@PostMapping
	public ResponseEntity<Void> createFunding(
		@RequestPart("fundingInfo") FundingInfoDTO fundingInfo,
		@RequestPart("rewards") List<RewardDTO> rewards,
		@RequestPart("artworks") List<ArtworkDTO> artwork,
		@RequestPart("mainImage") MultipartFile mainImage,
		@RequestPart("artworkImages") List<MultipartFile> artworkImages) {
		log.info("fundingInfo: {}", fundingInfo);
		fundingService.createFunding(fundingInfo, rewards, artwork, mainImage, artworkImages);
		return ResponseEntity.ok().build();
	}


	@GetMapping("/{fundingId}")
	public ResponseEntity<FundingDetailDTO> getFundingDetail(@PathVariable Long fundingId) {
		log.debug("fundingId: {}", fundingId);
		FundingDetailDTO fundingDetail = fundingService.getFundingDetail(fundingId);
		return ResponseEntity.ok(fundingDetail);
	}


}



