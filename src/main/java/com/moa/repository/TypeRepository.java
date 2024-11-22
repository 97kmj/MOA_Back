package com.moa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.Type;

public interface TypeRepository extends JpaRepository<Type, Integer> {
	List<Type> findByCategory_CategoryId(Integer categoryId);
}
