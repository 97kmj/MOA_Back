package com.moa.entity;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
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
import javax.persistence.PrePersist;

import com.moa.entity.User.ApprovalStatus;

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
	private String fundingMainImageUrl;
	private BigDecimal currentAmount;

	@Column(nullable = false)
	private Instant noticeDate;

	@Column(nullable = false,updatable = false)
	private Timestamp applicationDate;
	// private Date startDate;
	// private Date endDate;
	@Column(nullable = false)
	private Instant startDate; // 모집 시작 시간
	@Column(nullable = false)
	private Instant endDate;// 모집 종료 시간

	@Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @Enumerated(EnumType.STRING)
    private FundingStatus fundingStatus;

    public enum ApprovalStatus { PENDING, APPROVED, REJECTED }

    public enum FundingStatus {
		STANDBY,
		ONGOING,
		SUCCESSFUL,
		FAILED,
		CANCELLED;

		public boolean isRefundable() {
			return this == ONGOING || this == SUCCESSFUL;
		}
	}

	@PrePersist
	protected void onCreate() {
		this.applicationDate = new Timestamp(System.currentTimeMillis());
		if (this.startDate != null) {
			this.noticeDate = this.startDate.minus(Duration.ofDays(7)); // startDate 기준 7일 전
		}
		this.currentAmount = BigDecimal.ZERO;
	}

}
