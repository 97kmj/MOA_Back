package com.moa.shop.dto;

import com.moa.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
	private Integer categoryId;
	private String categoryName;
	
	public static Category toEntity(CategoryDto categoryDto) {
		Category category = Category.builder()
				.categoryId(categoryDto.getCategoryId())
				.categoryName(categoryDto.getCategoryName())
				.build();
		return category;
	}
	
	public static CategoryDto fromEntity(Category category) {
		CategoryDto categoryDto = CategoryDto.builder()
				.categoryId(category.getCategoryId())
				.categoryName(category.getCategoryName())
				.build();
		return categoryDto;
	}
	
}

