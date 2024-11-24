package com.moa.funding.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.moa.funding.dto.funding.ArtworkDTO;
import com.moa.funding.dto.funding.FundingInfoDTO;
import com.moa.funding.dto.funding.RewardDTO;
import com.moa.funding.service.FundingCreationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/funding")
public class FundingController {
	private final FundingCreationService fundingService;


	@PostMapping
	public ResponseEntity<Void> createFunding(
		@RequestPart("fundingInfo")FundingInfoDTO fundingInfo,
		@RequestPart("rewards") List<RewardDTO> rewards,
		@RequestPart("artworks") List<ArtworkDTO> artwork,
		@RequestPart("mainImage") MultipartFile mainImage,
		@RequestPart("artworkImages") List<MultipartFile> artworkImages
	) {
		try {
			// JSON 데이터 출력
			System.out.println("Funding Info JSON: " + fundingInfo);
			System.out.println("Rewards JSON: " + rewards);
			System.out.println("Artworks JSON: " + artwork);

			// 메인 이미지 출력
			System.out.println("Main Image: " + mainImage.getOriginalFilename());

			// 작품 이미지 출력
			for (MultipartFile artworkImage : artworkImages) {
				System.out.println("Artwork Image: " + artworkImage.getOriginalFilename());
			}

			fundingService.createFunding(fundingInfo, rewards, artwork, mainImage, artworkImages);

			return ResponseEntity.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
}