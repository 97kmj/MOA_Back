package com.moa.user.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.moa.entity.Funding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainFundingDto {
	private Long fundingId;
	private String fundingUserName;
	private String fundingMainImageUrl;
	private String title;
	private Date startDate;
	private Date endDate;
	private BigDecimal currentAmount;
	private BigDecimal goalAmount;
	
	public static MainFundingDto fromEntity(Funding funding) {
		return MainFundingDto.builder()
				.fundingId(funding.getFundingId())
				.fundingUserName(funding.getFundingUserName())
				.fundingMainImageUrl(funding.getFundingMainImageUrl())
				.title(funding.getTitle())
				.startDate(Date.from(funding.getStartDate()))
				.endDate(Date.from(funding.getEndDate()))
				.currentAmount(funding.getCurrentAmount())
				.goalAmount(funding.getGoalAmount())
				.build();
	}	
	
}
