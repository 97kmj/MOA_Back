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
	
	
	public Type toEntity() {
		Type type = Type.builder()
				.typeId(typeId)
				.typeName(typeName)
				.category(Category.builder().categoryId(categoryId).build())
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
