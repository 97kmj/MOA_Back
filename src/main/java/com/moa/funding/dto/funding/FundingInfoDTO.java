package com.moa.funding.dto.funding;



import java.math.BigDecimal;
import java.util.Date;

import com.moa.entity.User;

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
public class FundingInfoDTO {
	private String title;
	private User user;
	private String fundingUserName;
	private String description;
	private BigDecimal goalAmount;
	private Schedule schedule;
	private Registrant registrant;


	@Getter
	@Builder
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Schedule {
		private Date startDate;
		private Date endDate;
	}


	@Getter
	@Builder
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Registrant {
		private String name;
		private Account account;


		@Getter
		@Builder
		@ToString
		@NoArgsConstructor
		@AllArgsConstructor
		public static class Account {
			private String bank;
			private String account;
		}
	}

}
