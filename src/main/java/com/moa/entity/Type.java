package com.moa.entity;

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
public class Type {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer typeId;
	private String typeName;
	
	@ManyToOne
	@JoinColumn(name="category_id")
	private Category category;
}
