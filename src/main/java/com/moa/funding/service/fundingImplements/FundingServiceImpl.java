package com.moa.funding.service.fundingImplements;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.moa.config.image.FolderConstants;
import com.moa.config.image.ImageService;
import com.moa.entity.Funding;
import com.moa.entity.FundingImage;
import com.moa.entity.Reward;
import com.moa.funding.dto.funding.ArtworkDTO;
import com.moa.funding.dto.funding.FundingDetailDTO;
import com.moa.funding.dto.funding.FundingInfoDTO;
import com.moa.funding.dto.funding.RewardDTO;
import com.moa.funding.mapper.FundingCreationMapper;
import com.moa.funding.repository.FundingRepositoryCustom;
import com.moa.funding.repository.FundingRepositoryCustomImpl;
import com.moa.funding.service.FundingService;
import com.moa.repository.FundingImageRepository;
import com.moa.repository.FundingRepository;
import com.moa.repository.RewardRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FundingServiceImpl implements FundingService {
	private final FundingRepository fundingRepository;
	private final RewardRepository rewardRepository;
	private final FundingImageRepository fundingImageRepository;
	private final ImageService imageService;
	private final FundingRepositoryCustom fundingRepositoryCustom;

	@Override
	public FundingDetailDTO getFundingDetail(Long fundingId) {
		return fundingRepositoryCustom.findFundingDetailById(fundingId);
	}


	@Override
	public void createFunding(FundingInfoDTO fundingInfoDTO, List<RewardDTO> rewardDTOs, List<ArtworkDTO> artworkDTOs,
		MultipartFile mainImage, List<MultipartFile> artworkImages) {

		//Step1: Funding 생성 및 저장
		Funding funding = createFunding(fundingInfoDTO, mainImage);

		//Step2: Reward 생성 및 저장
		createRewards(rewardDTOs, funding);

		//Step3: Artwork 생성 및 저장
		createArtworks(artworkDTOs, artworkImages, funding);
	}


	private Funding createFunding(FundingInfoDTO fundingInfoDTO, MultipartFile mainImage) {
		// String fileType = "mainImage";
		String rootFolder = FolderConstants.FUNDING_ROOT; // "funding"
		String fileType = FolderConstants.FUNDING_MAIN_IMAGE; // "mainImage"

		String fundingMainImageUrl = null;
		// 이미지 파일 저장 및 정보 설정
		if (mainImage != null && !mainImage.isEmpty()) {
		fundingMainImageUrl = imageService.saveImage(rootFolder,fileType, mainImage);
		}

		Funding funding = FundingCreationMapper.toFundingEntity(fundingInfoDTO, fundingMainImageUrl);
		fundingRepository.save(funding);
		return funding;
	}

	private void createRewards(List<RewardDTO> rewardDTOs, Funding funding) {
		List<Reward> rewards = rewardDTOs.stream()
			.map(rewardDTO -> FundingCreationMapper.toRewardEntity(rewardDTO, funding))
			.collect(Collectors.toList());
		rewardRepository.saveAll(rewards);
	}

	private void createArtworks(List<ArtworkDTO> artworkDTOs, List<MultipartFile> artworkImages, Funding funding) {
		validateInputs(artworkDTOs, artworkImages);
		String rootFolder = FolderConstants.FUNDING_ROOT;
		String fileType = FolderConstants.FUNDING_ART_IMAGE;

		List<FundingImage> fundingImages = IntStream.range(0,artworkDTOs.size())
			.mapToObj(i -> createSingleFundingImage(artworkDTOs.get(i), artworkImages.get(i), funding, rootFolder, fileType))
			.collect(Collectors.toList());

		fundingImageRepository.saveAll(fundingImages);
	}

	// 단일 FundingImage 생성
	private FundingImage createSingleFundingImage(ArtworkDTO artworkDTO, MultipartFile artworkImage, Funding funding,
		String rootFolder, String fileType) {
		String fundingArtImageUrl = null;

		// 이미지 저장
		if (artworkImage != null && !artworkImage.isEmpty()) {
			fundingArtImageUrl = imageService.saveImage(rootFolder, fileType, artworkImage);
		}
		// FundingImage 생성
		return FundingCreationMapper.toFundingImageEntity(artworkImage, funding, artworkDTO, fundingArtImageUrl);
	}

	private void validateInputs(List<ArtworkDTO> artworkDTOs, List<MultipartFile> artworkImages) {
		if (artworkDTOs.size() != artworkImages.size()) {
			throw new IllegalArgumentException("ArtworkDTOs and artworkImages size 가 다르다 .");
		}
	}



}

