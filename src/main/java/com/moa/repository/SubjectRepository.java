package com.moa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

}
