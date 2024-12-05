package com.moa.user.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.moa.entity.MailVerification;

public interface MailVerificationRepository extends JpaRepository<MailVerification, Integer> {
	Optional<MailVerification> findByEmailAndCode(String email, String code);
	Optional<MailVerification> findByEmail(String email);
	void deleteByExpiresTimeBefore(LocalDateTime expiredDateTime);
}
