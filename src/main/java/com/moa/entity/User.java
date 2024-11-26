package com.moa.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;

import javax.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
	@Id
	private String username;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String name;
	private String nickname;
//	@Column(nullable = false, unique = true)
	private String email;
	private String phone;
	private String postcode;
	private String address;
	private String detailAddress;
	private String extraAddress;
	private String provider;
	private String providerId;

	private String portfolioUrl;
	@Lob
	private String artistNote;
	private String artistCareer;
	private String profileImage;
	
	@Enumerated(EnumType.STRING)
    private ApprovalStatus artistApprovalStatus;
	private Timestamp applicationDate;
	private Integer likeCount;
	@Enumerated(EnumType.STRING)
    private Role role;
	@Column(nullable = false, updatable = false)
	private Timestamp createAt;
	// 자동으로 생성 시간 설정

	@PrePersist
	protected void onCreate() {
		this.createAt = new Timestamp(System.currentTimeMillis());
		if (this.artistApprovalStatus == null) {
			this.artistApprovalStatus = ApprovalStatus.NORMAL; // 기본값 설정
		}
	}

	// Enums
    public enum ApprovalStatus { PENDING, APPROVED, NORMAL }
    public enum Role { USER, ARTIST, ADMIN }

}
