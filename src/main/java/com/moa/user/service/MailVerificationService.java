package com.moa.user.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moa.entity.MailVerification;
import com.moa.user.repository.MailVerificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class MailVerificationService {

    private final JavaMailSender emailSender;
	private final MailVerificationRepository mailVerificationRepository;

    public void sendEmail(String toEmail,String title, String content) throws Exception {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(toEmail);
        helper.setSubject(title);
        helper.setText(content, true); // true를 설정해서 HTML을 사용 가능하게 함
        helper.setReplyTo("imgforestmail@gmail.com"); // 회신 불가능한 주소 설정
        try {
            emailSender.send(message);
        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Unable to send email in sendEmail", e); // 원인 예외를 포함시키기
        }
    }

	public void sendCodeToEmail(String email) throws Exception {
        MailVerification mailVerification = createMailVerification(email);
        String title = "[MOA 이메일 인증 번호]";

        String content = "<html>"
                + "<body>"
                + "<h1>MOA 인증 코드: " + mailVerification.getCode() + "</h1>"
                + "<p>해당 코드를 홈페이지에 입력하세요.</p>"
                + "<footer style='color: grey; font-size: small;'>"
                + "<p>※본 메일은 자동응답 메일이므로 본 메일에 회신하지 마시기 바랍니다.</p>"
                + "</footer>"
                + "</body>"
                + "</html>";
        try {
            sendEmail(email, title, content);
        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Unable to send email in sendCodeToEmail", e); // 원인 예외를 포함시키기
        }
    }

    // 인증 코드 생성 및 저장
    public MailVerification createMailVerification(String email) {
        Optional<MailVerification> oMailVerification =  mailVerificationRepository.findByEmail(email);
        MailVerification mailVerification = null;
        String randomCode = generateRandomCode(6);
        if(oMailVerification.isPresent()) {
        	mailVerification = oMailVerification.get();
        	mailVerification.setCode(randomCode);
        } else {
        	mailVerification = MailVerification.builder()
                .email(email)
                .code(randomCode) // 랜덤 코드 생성
                .expiresTime(LocalDateTime.now().plusDays(1)) // 1일 후 만료
                .build();
        }
        return mailVerificationRepository.save(mailVerification);
    }

    public String generateRandomCode(int length) {
        // 숫자 + 대문자 + 소문자
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    // 인증 코드 유효성 검사
    public boolean verifyCode(String email, String code) throws Exception {
    	System.out.println(email);
    	System.out.println(code);
    	Optional<MailVerification> oVerificationCode = mailVerificationRepository.findByEmailAndCode(email, code);
    	if(oVerificationCode.isPresent()) {
    		MailVerification verificationCode = oVerificationCode.get();
    		if(verificationCode.getExpiresTime().isAfter(LocalDateTime.now())) {
    			mailVerificationRepository.deleteById(verificationCode.getNum());
    			return true;
    		}
    	}
        return false;
    }
}