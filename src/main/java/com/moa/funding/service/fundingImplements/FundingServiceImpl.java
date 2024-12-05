package com.moa.funding.service.fundingImplements;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.moa.config.image.FolderConstants;
import com.moa.config.image.ImageService;
import com.moa.entity.Funding;
import com.moa.entity.FundingImage;
import com.moa.entity.Reward;
import com.moa.funding.dto.funding.ArtworkDTO;
import com.moa.funding.dto.funding.FundingDetailDTO;
import com.moa.funding.dto.funding.FundingInfoDTO;
import com.moa.funding.dto.funding.FundingResponse;
import com.moa.funding.dto.funding.RewardDTO;
import com.moa.funding.mapper.FundingMapper;
import com.moa.funding.repository.FundingSelectRepositoryCustom;
import com.moa.funding.repository.FundingManagementRepositoryCustom;
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
	private final FundingSelectRepositoryCustom fundingSelectRepositoryCustom;
	private  final FundingManagementRepositoryCustom fundingManagementRepositoryCustom;


	@Scheduled(cron = "0 */10 * * * *") // 매 1분마다 실행
	@Transactional
	public void scheduleValidateFundingStatuses() {
		log.info("펀딩 상태 검증 스케줄링 시작");
		fundingManagementRepositoryCustom.validateAndUpdateFundingStatuses();
		log.info("펀딩 상태 검증 스케줄링 시작");
	}


	// @Scheduled(cron = "0 0 0 * * *") // 매일 0시 0분 0초에 실행
	// @Scheduled(cron = "0 0 * * * *")
	@Scheduled(cron = "0 */5 * * * *") //
	@Transactional
	public void scheduleUpdateToOngoing() {
		log.info("scheduleUpdateToOngoing 스케줄링 실행- ONGOING 상태 변경 시작 " );
		fundingManagementRepositoryCustom.updateFundingToOnGoing();
		log.info("scheduleUpdateToOngoing 스케줄링 실행- ONGOING 상태 변경 완료 " );
	}

	// @Scheduled(cron = "0 0 * * * *") // 매시간 0분 0초에 실행
	// @Scheduled(cron = "0 30 * * * *") // 매시간 30분에 실행
	@Scheduled(cron = "0 */5 * * * *")
	@Transactional
	public void scheduleUpdateToSuccessful() {
		log.info("scheduleUpdateToSuccessful 스케줄링 실행 - SUCCESSFUL 상태 변경 시작 " );
		fundingManagementRepositoryCustom.updateFundingToSuccessful();
		log.info("scheduleUpdateToSuccessful 스케줄링 실행 - SUCCESSFUL 상태 변경 완료 " );
	}

	@Scheduled(cron = "0 */5 * * * *") //
	@Transactional
	public void scheduleUpdateRefundedToOngoing() {
		log.info("scheduleUpdateRefundedToOngoing 스케줄링 실행 - REFUNDED 상태 변경 시작 " );
		fundingManagementRepositoryCustom.updateFundingToOngoingIfRefund();
		log.info("scheduleUpdateRefundedToOngoing 스케줄링 실행 - REFUNDED 상태 변경 완료 " );
	}



	@Override
	public void createFunding(FundingInfoDTO fundingInfoDTO, List<RewardDTO> rewardDTOs, List<ArtworkDTO> artworkDTOs,
		MultipartFile mainImage, List<MultipartFile> artworkImages) {
		log.info("펀딩 생성 시작 - Title: {}", fundingInfoDTO.getTitle());

		//Step1: Funding 생성 및 저장
		Funding funding = createFunding(fundingInfoDTO, mainImage);
		log.info("펀딩 저장 완료 - ID: {}", funding.getFundingId());

		//Step2: Reward 생성 및 저장
		createRewards(rewardDTOs, funding);
		log.info("리워드 저장 완료 - 펀딩 ID: {}", funding.getFundingId());

		//Step3: Artwork 생성 및 저장
		createArtworks(artworkDTOs, artworkImages, funding);
		log.info("아트워크 저장 완료 - 펀딩 ID: {}", funding.getFundingId());
	}

	private Funding createFunding(FundingInfoDTO fundingInfoDTO, MultipartFile mainImage) {
		String rootFolder = FolderConstants.FUNDING_ROOT; // "funding"
		String fileType = FolderConstants.FUNDING_MAIN_IMAGE; // "mainImage"

		String fundingMainImageUrl = null;
		// 이미지 파일 저장 및 정보 설정
		if (mainImage != null && !mainImage.isEmpty()) {
			fundingMainImageUrl = imageService.saveImage(rootFolder, fileType, mainImage);
		}

		Funding funding = FundingMapper.toFundingEntity(fundingInfoDTO, fundingMainImageUrl);
		fundingRepository.save(funding);
		return funding;
	}


	@Override
	public FundingDetailDTO getFundingDetail(Long fundingId) {
		return fundingSelectRepositoryCustom.findFundingDetailById(fundingId);
	}

	@Override
	public FundingResponse getFundingList(String filterType, String sortOption, int page) {
		return fundingSelectRepositoryCustom.findFundingList(filterType, sortOption, page);

	}



	private void createRewards(List<RewardDTO> rewardDTOs, Funding funding) {
		List<Reward> rewards = rewardDTOs.stream()
			.map(rewardDTO -> FundingMapper.toRewardEntity(rewardDTO, funding))
			.collect(Collectors.toList());
		rewardRepository.saveAll(rewards);
	}

	private void createArtworks(List<ArtworkDTO> artworkDTOs, List<MultipartFile> artworkImages, Funding funding) {
		validateInputs(artworkDTOs, artworkImages);
		String rootFolder = FolderConstants.FUNDING_ROOT;
		String fileType = FolderConstants.FUNDING_ART_IMAGE;

		List<FundingImage> fundingImages = IntStream.range(0, artworkDTOs.size())
			.mapToObj(
				i -> createSingleFundingImage(artworkDTOs.get(i), artworkImages.get(i), funding, rootFolder, fileType))
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
		return FundingMapper.toFundingImageEntity(artworkImage, funding, artworkDTO, fundingArtImageUrl);
	}

	private void validateInputs(List<ArtworkDTO> artworkDTOs, List<MultipartFile> artworkImages) {
		if (artworkDTOs.size() != artworkImages.size()) {
			throw new IllegalArgumentException("ArtworkDTOs and artworkImages size 가 다르다 .");
		}
	}

}

