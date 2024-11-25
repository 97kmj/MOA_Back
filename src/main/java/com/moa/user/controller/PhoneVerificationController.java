package com.moa.user.controller;

import com.moa.user.service.TwilioSmsService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/phone")
public class PhoneVerificationController {

    private final TwilioSmsService smsService;
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>(); // 전화번호별 인증 코드 저장

    public PhoneVerificationController(TwilioSmsService smsService) {
        this.smsService = smsService;
    }

    //코드 전송
    @PostMapping("/send-code")
    public String sendVerificationCode(@RequestBody String phoneNumber) {
        // 인증 코드 생성
        String verificationCode = smsService.generateVerificationCode();
        //저장
        verificationCodes.put(phoneNumber, verificationCode);
        //발송
        smsService.sendSms(phoneNumber, verificationCode);
        System.out.println("Generated Code: " + verificationCode);
        return "Verification code sent successfully!";
    }

    //코드 검증
    @PostMapping("/verify-code")
    public String verifyCode(@RequestBody VerificationRequest request) {
        String phoneNumber = request.getPhoneNumber();
        String inputCode = request.getCode();

        // 저장된 인증 코드 확인
        String savedCode = verificationCodes.get(request.getPhoneNumber());

        if (savedCode != null && savedCode.equals(inputCode)) {
            // 인증 성공 시 코드 삭제
            verificationCodes.remove(phoneNumber);
            return "Verification successful!";
        }
        return "Verification failed!";
    }

    // 내부 클래스: 요청 데이터 객체
    static class VerificationRequest {
        private String phoneNumber;
        private String code;

        // Getter & Setter
        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}