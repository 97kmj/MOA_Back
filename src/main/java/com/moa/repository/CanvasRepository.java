package com.moa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.Canvas;

public interface CanvasRepository extends JpaRepository<Canvas, Long> {
	List<Canvas> findAll();
}
