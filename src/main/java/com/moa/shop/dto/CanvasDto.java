package com.moa.shop.dto;

import com.moa.entity.Canvas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CanvasDto {
	
	private Long canvasId;
	private String canvasNum;
	
	public static Canvas toEntity(CanvasDto canvasDto) {
		Canvas canvas = Canvas.builder()
				.canvasId(canvasDto.getCanvasId())
				.canvasNum(Canvas.CanvasNum.fromValue(canvasDto.getCanvasNum()))
				.build();
		return canvas;					
	}
	
	public static CanvasDto fromEntity(Canvas canvas) {
		CanvasDto canvasDto = CanvasDto.builder()
				.canvasId(canvas.getCanvasId())
				.canvasNum(canvas.getCanvasNum().getValue())
				.build();
		return canvasDto;
				
	}
}
