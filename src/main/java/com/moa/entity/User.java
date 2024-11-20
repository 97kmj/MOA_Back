package com.moa.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;

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
	@Column(nullable = false, unique = true)
	private String email;
	private String phone;
	private String postcode;
	private String address;
	private String detailAddress;
	private String extraAddress;
	private String googleId;
	private String kakaoId;
	private String naverId;
	private String socialProvider;
	
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

	private Timestamp createAt;


	// Enums
    public enum ApprovalStatus { PENDING, APPROVED, NORMAL }
    public enum Role { USER, ARTIST, ADMIN }

}
