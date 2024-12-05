package com.moa.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.SmsVerification;

public interface SmsVerificationRepository extends JpaRepository<SmsVerification, Integer> {
	Optional<SmsVerification> findByPhone(String phone);
	Optional<SmsVerification> findByPhoneAndCode(String phone, String code);
}
