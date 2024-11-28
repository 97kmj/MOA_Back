package com.moa.shop.dto;

import com.moa.entity.Canvas;
import com.moa.entity.Canvas.CanvasType;

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
	private CanvasType canvasType;
    private String width;
    private String height;
	
	public Canvas toEntity() {
		Canvas canvas = Canvas.builder()
				.canvasId(getCanvasId())
				.canvasNum(Canvas.CanvasNum.fromValue(canvasNum))
				.canvasType(getCanvasType())
				.width(getWidth())
				.height(getHeight())
				.build();
		return canvas;					
	}
	
	public static CanvasDto fromEntity(Canvas canvas) {
		CanvasDto canvasDto = CanvasDto.builder()
				.canvasId(canvas.getCanvasId())
				.canvasNum(canvas.getCanvasNum().getValue())
				.canvasType(canvas.getCanvasType())
				.width(canvas.getWidth())
				.height(canvas.getHeight())
				.build();
		return canvasDto;
				
	}
}
