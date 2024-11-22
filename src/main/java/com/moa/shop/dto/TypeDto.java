package com.moa.shop.dto;

import com.moa.entity.Category;
import com.moa.entity.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeDto {
	private Integer typeId;
	private String typeName;
	private Integer categoryId;
	
	
	public static Type toEntity(TypeDto typeDto) {
		Type type = Type.builder()
				.typeId(typeDto.getTypeId())
				.typeName(typeDto.getTypeName())
				.category(Category.builder().categoryId(typeDto.getCategoryId()).build())
				.build();
		return type;			
	}
	
	public static TypeDto fromEntity(Type type) {
		TypeDto typeDto = TypeDto.builder()
				.typeId(type.getTypeId())
				.typeName(type.getTypeName())
				.categoryId(type.getCategory().getCategoryId())
				.build();
		return typeDto;	
			
				
	}
	
}
