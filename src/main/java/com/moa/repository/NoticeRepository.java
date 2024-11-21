package com.moa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
	Page<Notice> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
