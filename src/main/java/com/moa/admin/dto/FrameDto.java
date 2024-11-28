package com.moa.admin.dto;


import com.moa.entity.Canvas.CanvasType;
import com.moa.entity.FrameOption;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FrameDto {
	private Long frameId;	
	private String frameType;
	private Long framePrice;
	private Integer stock;
	private String canvasNum;
	private CanvasType canvasType;
	
	public static FrameDto fromEntity(FrameOption frameOption) {
		return FrameDto.builder()
					.frameId(frameOption.getFrameOptionId())
					.frameType(frameOption.getFrameType())
					.framePrice(frameOption.getFramePrice())
					.canvasNum(frameOption.getCanvas().getCanvasNum().getValue())
					.canvasType(frameOption.getCanvas().getCanvasType())
					.stock(frameOption.getStock())
					.build();
	}
	
}
