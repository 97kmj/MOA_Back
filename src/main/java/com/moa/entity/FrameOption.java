package com.moa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FrameOption {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long frameOptionId;

	@Column(length = 255, nullable = false)
	private String frameType;

	@Column(nullable = false)
	private Long framePrice;

	@ManyToOne
    @JoinColumn(name = "canvas_id", nullable = false) // 외래 키 설정
    private Canvas canvas;

	@Column(nullable = false)
	private Integer stock;

}
