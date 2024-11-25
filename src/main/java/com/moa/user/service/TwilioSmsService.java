package com.moa.user.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TwilioSmsService {

    // Twilio 계정 정보
    private static final String ACCOUNT_SID = "AC7374f3c46223580dfbb16704b716c2fc";
    private static final String AUTH_TOKEN = "9b922714eaf5077e1bb022591d6e3c93";
    private static final String TWILIO_PHONE_NUMBER = "+19787234438";

    static {
        // Twilio 초기화
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    // 인증 코드 생성
    public String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000)); // 6자리 랜덤 코드
    }

    // 전화번호를 국제 형식으로 변환
    private String formatToInternationalPhoneNumber(String phoneNumber) {
        // 대한민국 국가 코드 +82 적용
        if (phoneNumber.startsWith("0")) {
            phoneNumber = "+82" + phoneNumber.substring(1); // 0을 제외하고 +82 추가
        }
        return phoneNumber.replaceAll("[^+0-9]", ""); // 숫자와 "+"만 남김
    }

    // SMS 전송
    public void sendSms(String toPhoneNumber, String verificationCode) {
        String formattedNumber = formatToInternationalPhoneNumber(toPhoneNumber);

        try {
            Message message = Message.creator(
                new PhoneNumber(formattedNumber), // 변환된 번호
                new PhoneNumber(TWILIO_PHONE_NUMBER), // Twilio 발신 번호
                "Your verification code is: " + verificationCode // 메시지 내용
            ).create();

            System.out.println("SMS sent successfully to: " + formattedNumber);
            System.out.println("Message SID: " + message.getSid());
        } catch (Exception e) {
            System.err.println("Failed to send SMS to: " + formattedNumber);
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }
}
