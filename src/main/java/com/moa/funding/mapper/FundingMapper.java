package com.moa.funding.mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

import org.springframework.web.multipart.MultipartFile;

import com.moa.entity.Funding;
import com.moa.entity.FundingImage;
import com.moa.entity.Reward;
import com.moa.funding.dto.funding.ArtworkDTO;
import com.moa.funding.dto.funding.FundingInfoDTO;
import com.moa.funding.dto.funding.FundingListDTO;
import com.moa.funding.dto.funding.RewardDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FundingMapper {
	private FundingMapper() {
	}

	//펀딩 저장 시 사용 - toFundingEntity
	public static Funding toFundingEntity(FundingInfoDTO fundingInfoDTO,  String fundingMainImageUrl) {
		Instant startDate = fundingInfoDTO.getSchedule()
			.getStartDate()
			.atStartOfDay(ZoneOffset.UTC) // 시작일의 00:00:00 UTC
			.toInstant();

		Instant endDate = fundingInfoDTO.getSchedule()
			.getEndDate()
			.atTime(23, 59, 59) // 종료일의 23:59:59 UTC
			.atZone(ZoneOffset.UTC)
			.toInstant();

		return Funding.builder()
			.user(fundingInfoDTO.getUser())
			.fundingUserName(fundingInfoDTO.getRegistrant().getName())
			.accountNumber(fundingInfoDTO.getRegistrant().getAccount().getAccount())
			.bankName(fundingInfoDTO.getRegistrant().getAccount().getBank())
			.goalAmount(fundingInfoDTO.getGoalAmount())
			.title(fundingInfoDTO.getTitle())
			.introduction(fundingInfoDTO.getDescription())
			.startDate(startDate)
			.endDate(endDate)
			.fundingMainImageUrl(fundingMainImageUrl)
			.applicationDate(new Timestamp(System.currentTimeMillis()))
			.approvalStatus(Funding.ApprovalStatus.PENDING)
			.fundingStatus(Funding.FundingStatus.STANDBY)
			.build();
	}

	//리워드 저장 시 사용 - toRewardEntity
	public static Reward toRewardEntity(RewardDTO rewardDTO, Funding funding) {
		return Reward.builder()
			.funding(funding)
			.rewardName(rewardDTO.getName())
			.rewardDescription(rewardDTO.getDescription())
			.rewardPrice(rewardDTO.getPrice())
			.stock(rewardDTO.getQuantity())
			.isLimit(rewardDTO.getIsQuantityLimited())
			.limitQuantity(rewardDTO.getLimitPerPerson())
			.rewardType(Reward.RewardType.valueOf(rewardDTO.getRewardType()))
			.build();
	}

	//펀딩의 작품이미지  저장 시 사용 - toFundingImageEntity
	public static FundingImage toFundingImageEntity(MultipartFile image, Funding funding, ArtworkDTO artworkDTO, String fundingArtImageUrl) {
		return FundingImage.builder()
			.funding(funding)
			.title(artworkDTO.getTitle())
			.introduction(artworkDTO.getDescription())
			.imageUrl(fundingArtImageUrl)
			.build();
	}

	public static FundingListDTO toFundingListDTO(Funding funding) {
		return FundingListDTO.builder()
			.fundingId(funding.getFundingId())
			.title(funding.getTitle())
			.fundingMainImageUrl(funding.getFundingMainImageUrl())
			.goalAmount(funding.getGoalAmount())
			.totalAmount(funding.getCurrentAmount())
			.startDate(funding.getStartDate())
			.endDate(funding.getEndDate())
			.fundingStatus(funding.getFundingStatus().name())
			.achievementRate(calculateAchievementRate(funding))
			.remainingDays(calculateRemainingDays(funding.getEndDate()))
			.build();
	}

	//달성률 계산
	private static int calculateAchievementRate(Funding funding) {
		BigDecimal goalAmount = funding.getGoalAmount();
		BigDecimal currentAmount = funding.getCurrentAmount();

		//목표금액이 0보다 큰지 확인
		if (goalAmount.compareTo(BigDecimal.ZERO) > 0) {
			// (현재 모금액 / 목표금액) * 100
			BigDecimal rate = currentAmount.multiply(BigDecimal.valueOf(100))
				.divide(goalAmount, RoundingMode.FLOOR);
			return rate.intValue();
		}else {
			// 목표금액이 0인 경우 달성률 0
			return 0;
		}
	}

	// 남은 날짜 계산
	private static int calculateRemainingDays(Instant endDate) {
		Instant today = Instant.now();
		// 오늘 날짜와 종료 날짜의 차이 계산
		long daysBetween = Duration.between(today, endDate).toDays();

		// 남은 날짜가 0 미만이면 0 반환
		return (int) Math.max(daysBetween, 0);
	}


}