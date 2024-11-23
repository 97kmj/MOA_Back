package com.moa.funding.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/funding")
public class FundingController {

	@PostMapping
	public ResponseEntity<Void> createFunding(
		@RequestPart("fundingInfo") String fundingInfo,
		@RequestPart("rewards") String rewards,
		@RequestPart("artworks") String artwork,
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

			// 필요한 비즈니스 로직 실행
			// 예: 서비스 계층에 데이터 전달 및 저장 로직

			return ResponseEntity.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
}