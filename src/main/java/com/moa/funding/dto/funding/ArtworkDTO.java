package com.moa.funding.dto.funding;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ArtworkDTO {
	private String title;
	private String description;

}
