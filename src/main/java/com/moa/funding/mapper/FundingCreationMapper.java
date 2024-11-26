package com.moa.funding.mapper;

import java.sql.Timestamp;

import org.springframework.web.multipart.MultipartFile;

import com.moa.entity.Funding;
import com.moa.entity.FundingImage;
import com.moa.entity.Reward;
import com.moa.funding.dto.funding.ArtworkDTO;
import com.moa.funding.dto.funding.FundingInfoDTO;
import com.moa.funding.dto.funding.RewardDTO;

public class FundingCreationMapper {
	private FundingCreationMapper() {
	}

	public static Funding toFundingEntity(FundingInfoDTO fundingInfoDTO,  String fundingMainImageUrl) {
		return Funding.builder()
			.user(fundingInfoDTO.getUser())
			.fundingUserName(fundingInfoDTO.getUser().getUsername())
			.accountNumber(fundingInfoDTO.getRegistrant().getAccount().getAccount())
			.bankName(fundingInfoDTO.getRegistrant().getAccount().getBank())
			.goalAmount(fundingInfoDTO.getGoalAmount())
			.title(fundingInfoDTO.getTitle())
			.introduction(fundingInfoDTO.getDescription())
			.startDate(fundingInfoDTO.getSchedule().getStartDate())
			.endDate(fundingInfoDTO.getSchedule().getEndDate())
			.fundingMainImageUrl(fundingMainImageUrl)
			.applicationDate(new Timestamp(System.currentTimeMillis()))
			.approvalStatus(Funding.ApprovalStatus.PENDING)
			.fundingStatus(Funding.FundingStatus.STANDBY)
			.build();
	}

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

	public static FundingImage toFundingImageEntity(MultipartFile image, Funding funding, ArtworkDTO artworkDTO, String fundingArtImageUrl) {
		return FundingImage.builder()
			.funding(funding)
			.title(artworkDTO.getTitle())
			.introduction(artworkDTO.getDescription())
			.imageUrl(fundingArtImageUrl)
			.build();
	}
}