package com.moa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
