package com.moa.entity;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Funding {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fundingId;
	@ManyToOne
	@JoinColumn(name="username")
	private User user;
	@Column(nullable = false)
	private String fundingUserName;
	private String accountNumber;
	private String bankName;
	
	@Column(nullable = false)
	private BigDecimal goalAmount;
	@Column(nullable = false)
	private String title;
	@Lob
	private String introduction;
	private String fundingMainImage;
	private String fundingMainImageUrl;
	private Date noticeDate;
	private Long currentAmount;
	
	@Column(nullable = false,updatable = false)
	private Timestamp applicationDate;
	private Date startDate;
	private Date endDate;

	@Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @Enumerated(EnumType.STRING)
    private FundingStatus fundingStatus;

    public enum ApprovalStatus { PENDING, APPROVED, REJECTED }
    public enum FundingStatus { STANDBY, ONGOING, SUCCESSFUL, FAILED, CANCELLED }

	
	
}
