package com.moa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.Subject;


public interface SubjectRepository extends JpaRepository<Subject, Integer> {
	List<Subject> findByCategory_CategoryId(Integer categoryId);
}
