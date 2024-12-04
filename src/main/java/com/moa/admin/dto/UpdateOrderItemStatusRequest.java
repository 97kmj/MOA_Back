package com.moa.admin.dto;

import lombok.Data;

@Data
public class UpdateOrderItemStatusRequest {
	private Long orderItemId;
	private String status;
}
