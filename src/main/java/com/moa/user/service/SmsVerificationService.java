package com.moa.user.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moa.entity.SmsVerification;
import com.moa.user.repository.SmsVerificationRepository;

import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

@Transactional
@Service
@RequiredArgsConstructor
public class SmsVerificationService {
	private final SmsVerificationRepository smsVerificationRepository;
	
    @Value("${coolsms.api-key}")
    private String apiKey;

    @Value("${coolsms.api-secret}")
    private String apiSecret;
    
    @Value("${coolsms.from-phone}")
    private String fromPhone;

    public Boolean sendSms(String phone)  throws Exception {
    	DefaultMessageService messageService =  NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    	SmsVerification smsVerification = createSmsVerification(phone);
    	Message message = new Message();
    	message.setFrom(fromPhone);
    	message.setTo(smsVerification.getPhone());
    	message.setText("[MOA] 인증번호 [" + smsVerification.getCode() + "]를 입력해주세요");
    	SingleMessageSendingRequest singleMessageSendingRequest = new SingleMessageSendingRequest(message);
		try {
			SingleMessageSentResponse singleMessageSentResponse = messageService.sendOne(singleMessageSendingRequest);
			System.out.println(singleMessageSentResponse);
			return true;
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			return false;
		}
    }
    
    // 인증 코드 생성 및 저장
    public SmsVerification createSmsVerification(String phone) {
        Optional<SmsVerification> oSmsVerification =  smsVerificationRepository.findByPhone(phone);
        SmsVerification smsVerification = null;
        String randomCode = createSmsKey();
        if(oSmsVerification.isPresent()) {
        	smsVerification = oSmsVerification.get();
        	smsVerification.setCode(randomCode);
        	smsVerification.setExpiresTime(LocalDateTime.now().plusDays(1));
        } else {
        	smsVerification = SmsVerification.builder()
                .phone(phone)
                .code(randomCode) // 랜덤 코드 생성
                .expiresTime(LocalDateTime.now().plusDays(1)) // 1일 후 만료
                .build();
        }
        return smsVerificationRepository.save(smsVerification);
    }    
    
	// 인증코드 만들기
	public static String createSmsKey() {
		StringBuffer key = new StringBuffer();
		Random rnd = new Random();

		for (int i = 0; i < 6; i++) { // 인증코드 6자리
			key.append((rnd.nextInt(10)));
		}
		return key.toString();
	}    
	
    // 인증 코드 유효성 검사
    public boolean verifyCode(String sms, String code) throws Exception {
    	System.out.println(sms);
    	System.out.println(code);
    	Optional<SmsVerification> oVerificationCode = smsVerificationRepository.findByPhoneAndCode(sms, code);
    	System.out.println(oVerificationCode);
    	if(oVerificationCode.isPresent()) {
    		SmsVerification verificationCode = oVerificationCode.get();
    		if(verificationCode.getExpiresTime().isAfter(LocalDateTime.now())) {
    			smsVerificationRepository.deleteById(verificationCode.getNum());
    			return true;
    		}
    	}
        return false;
    }
}