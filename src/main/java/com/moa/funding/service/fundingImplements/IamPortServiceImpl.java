package com.moa.funding.service.fundingImplements;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import com.moa.funding.service.IamPortService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IamPortServiceImpl implements IamPortService {

	private final IamportClient iamportClient;

	// 생성자에서 API 키와 Secret을 설정
	public IamPortServiceImpl() {
		String apiKey = "7361065381668827"; // 아임포트 관리자 콘솔에서 제공된 API Key
		String apiSecret = "pyknx2AnQz3so0G0XVbfiMLmThNGaV9YbXLoaJeUxCKWEc9kQrL8W23NqibYzM28faPFYsNaX0xmZFbi"; // 아임포트 관리자 콘솔에서 제공된 API Secret
		this.iamportClient = new IamportClient(apiKey, apiSecret);
	}

	@Override
	public boolean verifyPayment(Long amount, String impUid) {
		try {
			// 아임포트 서버에서 결제 정보 가져오기
			IamportResponse<Payment> paymentResponse = iamportClient.paymentByImpUid(impUid);

			if (paymentResponse != null && paymentResponse.getResponse() != null) {
				Payment payment = paymentResponse.getResponse();
				// 결제 금액 검증
				return payment.getAmount().compareTo(BigDecimal.valueOf(amount)) == 0;
			}
		} catch (Exception e) {
			log.error("아임포트 결제 검증 중 오류 발생: {}", e.getMessage(), e);
		}

		return false;
	}

	}
