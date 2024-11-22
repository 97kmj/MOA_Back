package com.moa.funding.dto.payment.webhook;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.moa.funding.dto.payment.RewardRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortOneCustomData {
	private Long fundingId; // 펀딩 ID
	private List<RewardRequest> rewardList; // 리워드 정보 리스트

	public PortOneCustomData() {
	}
}


