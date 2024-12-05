package com.moa.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MailVerification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer num;
	private String email;
	private String code;
	private LocalDateTime expiresTime;
}
