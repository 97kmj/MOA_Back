package com.moa.funding.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.moa.funding.dto.funding.ArtworkDTO;
import com.moa.funding.dto.funding.FundingDetailDTO;
import com.moa.funding.dto.funding.FundingInfoDTO;
import com.moa.funding.dto.funding.RewardDTO;
import com.moa.funding.service.FundingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/funding")
public class FundingController {
	private final FundingService fundingService;


	@PostMapping
	public ResponseEntity<Void> createFunding(
		@RequestPart("fundingInfo")FundingInfoDTO fundingInfo,
		@RequestPart("rewards") List<RewardDTO> rewards,
		@RequestPart("artworks") List<ArtworkDTO> artwork,
		@RequestPart("mainImage") MultipartFile mainImage,
		@RequestPart("artworkImages") List<MultipartFile> artworkImages
	) {
		try {
			log.info("fundingInfo: {}", fundingInfo);
			log.debug("rewards: {}", rewards);
			log.debug("artwork: {}", artwork);
			log.debug("mainImage: {}", mainImage);
			log.debug("artworkImages: {}", artworkImages);

			fundingService.createFunding(fundingInfo, rewards, artwork, mainImage, artworkImages);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/{fundingId}")
	public ResponseEntity<FundingDetailDTO> getFundingDetail(@PathVariable Long fundingId) {
		try {
			log.info("fundingId: {}", fundingId);
			FundingDetailDTO fundingDetail = fundingService.getFundingDetail(fundingId);
			return ResponseEntity.ok(fundingDetail);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

}