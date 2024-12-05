package com.moa.user.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moa.user.dto.VerifyResponseDto;
import com.moa.user.service.MailVerificationService;
import com.moa.user.service.SmsVerificationService;
import com.moa.user.service.UserService;

import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/verification")
public class VerificationController {
    private final MailVerificationService mailVerificationService;
    private final SmsVerificationService smsVerificationService;
    private final UserService userService;

    //인증 번호 전송
    @PostMapping("/send-email")
    public ResponseEntity<Boolean> sendEmail(@RequestBody Map<String,String> param) {
    	try {
    		mailVerificationService.sendCodeToEmail(param.get("email"));
    		return new ResponseEntity<>(true, HttpStatus.OK);
    	} catch(Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }

    //이메일 인증
    @PostMapping("/verify-email")
    public ResponseEntity<VerifyResponseDto> verifyEmail(@RequestBody Map<String,String> param) {
    	try {
			boolean isVerified = mailVerificationService.verifyCode(param.get("email"),param.get("verificationCode"));
			VerifyResponseDto responseDto = new VerifyResponseDto();
			responseDto.setVerified(isVerified);
			responseDto.setUsername(userService.usernameByEmail(param.get("email")));			
			responseDto
					.setMessage(isVerified ? "Email verified successfully." : "Invalid or expired verification code.");
	        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    	} catch(Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }	
    
    @PostMapping("/confirmId")
    public ResponseEntity<Boolean> confirmId(@RequestBody Map<String, String> param) {
    	try {
    		return new ResponseEntity<>(userService.confirmUsernsem(param.get("username")),HttpStatus.OK);
    	} catch(Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Boolean> changePassword(@RequestBody Map<String, String> param) {
    	try {
    		userService.changePassword(param.get("username"),param.get("password"));
    		return new ResponseEntity<>(true,HttpStatus.OK);
    	} catch(Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }
    
    @PostMapping("/send-sms")
    public ResponseEntity<Boolean> sendSms(@RequestBody Map<String, String> param) {
    	try {
    		smsVerificationService.sendSms(param.get("sms"));
    		return new ResponseEntity<>(true, HttpStatus.OK);
    	} catch(Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }
    
    //이메일 인증
    @PostMapping("/verify-sms")
    public ResponseEntity<VerifyResponseDto> verifySms(@RequestBody Map<String,String> param) {
    	System.out.println(param);
    	try {
			boolean isVerified = smsVerificationService.verifyCode(param.get("sms"),param.get("verificationCode"));
			VerifyResponseDto responseDto = new VerifyResponseDto();
			responseDto.setVerified(isVerified);
			if(isVerified) responseDto.setUsername(userService.usernameByPhone(param.get("sms")));			
			responseDto
					.setMessage(isVerified ? "Sms verified successfully." : "Invalid or expired verification code.");
	        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    	} catch(Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }
}
