package com.moa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.Canvas;

public interface CanvasRepository extends JpaRepository<Canvas, Long> {

}
