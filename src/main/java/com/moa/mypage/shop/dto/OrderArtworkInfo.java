package com.moa.mypage.shop.dto;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderArtworkInfo {
	private String userName;
	private Timestamp startDate;
	private Timestamp endDate;
	private Integer page;
	private Integer size;
  
}
