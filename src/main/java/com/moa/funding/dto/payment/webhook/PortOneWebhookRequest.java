package com.moa.funding.dto.payment.webhook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PortOneWebhookRequest {
	private String impUid;         // 아임포트 결제 고유 ID
	private String merchantUid;    // 상점 거래 ID
	private String status;         // 결제 상태 (paid, failed, canceled)
	private Long amount;           // 결제 금액 (옵션)
}
