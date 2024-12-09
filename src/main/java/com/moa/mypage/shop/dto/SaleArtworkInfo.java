package com.moa.mypage.shop.dto;



import java.util.Date;

import com.moa.entity.Artwork.SaleStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleArtworkInfo {
	private String userName;
	private SaleStatus saleStatus;
	private Date startDate;
	private Date endDate;
	private Integer page;
	private Integer size;
}
