package com.moa.funding.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class IamPortResponse {
	@JsonProperty("response")
	private PaymentInfo response;

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class PaymentInfo {
		@JsonProperty("imp_uid")
		private String impUid; // 아임포트 결제 고유 ID

		@JsonProperty("merchant_uid")
		private String merchantUid; // 가맹점 주문 ID

		@JsonProperty("amount")
		private Long amount; // 결제된 금액

		@JsonProperty("status")
		private String status; // 결제 상태 (예: paid, failed 등)

		@JsonProperty("paid_at")
		private Long paidAt; // 결제 완료 시간 (UNIX 타임스탬프)
	}
}

