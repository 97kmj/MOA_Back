package com.moa.admin.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.moa.entity.Funding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundingApplyDto {
	private Long fundingId;
	private BigDecimal goalAmount;
	private Timestamp applicationDate;
	private Date startDate;
	private Date endDate;
	private String introduction;
	private String fundingMainImageUrl;
	private List<String> imageUrlList;
	private String username;
	private String fundingUserName;
	private List<FundingRewardDto> rewardList;
	
	
	public static FundingApplyDto fromFunding(Funding funding) {
		return FundingApplyDto.builder()
					.fundingId(funding.getFundingId())
					.goalAmount(funding.getGoalAmount())
					.applicationDate(funding.getApplicationDate())
					.startDate(funding.getStartDate())
					.endDate(funding.getEndDate())
					.introduction(funding.getIntroduction())
					.fundingMainImageUrl(funding.getFundingMainImageUrl())
					.username(funding.getUser().getUsername())
					.fundingUserName(funding.getFundingUserName())
					.build();
	}
}
