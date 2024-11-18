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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer subjectId;
	private String subjectName;
	
	@ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
	
}
