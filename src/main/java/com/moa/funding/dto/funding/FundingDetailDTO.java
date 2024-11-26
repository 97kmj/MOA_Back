package com.moa.funding.dto.funding;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FundingDetailDTO {
	private Long fundingId;
	private String title;
	private String fundingMainImageUrl;
	private String fundingUserName;
	private BigDecimal totalAmount;
	private BigDecimal goalAmount;
	private Date startDate;
	private Date endDate;
	private String introduction;
	private List<RewardDTO> rewards;
	private List<ImageDTO> images;


	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class RewardDTO {
		private Long rewardId;
		private String rewardName;
		private String rewardDescription;
		private BigDecimal rewardPrice;
		private Integer stock;
		private Boolean isLimit;
		private Integer limitQuantity;
		private String rewardType;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ImageDTO {
		private Long imageId;
		private String title;
		private String introduction;
		private String imageUrl;
	}

}
