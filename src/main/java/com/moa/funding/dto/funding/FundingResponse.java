package com.moa.funding.dto.funding;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FundingResponse {
	private List<FundingListDTO> fundingList;
	private boolean isLastPage;
}