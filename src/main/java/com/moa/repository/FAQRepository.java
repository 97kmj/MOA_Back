package com.moa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.FAQ;

public interface FAQRepository extends JpaRepository<FAQ, Long> {

}
