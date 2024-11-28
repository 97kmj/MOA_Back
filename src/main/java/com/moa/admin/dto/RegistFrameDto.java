package com.moa.admin.dto;



import com.moa.entity.FrameOption;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistFrameDto {
	private String frameType;
	private Long framePrice;
	private Integer stock;
	private Long canvasId;
	
	
}
