package com.moa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.moa.admin.dto.FrameDto;

import com.moa.entity.FrameOption;

public interface FrameOptionRepository extends JpaRepository<FrameOption, Long> {

	 List<FrameOption> findByCanvasCanvasId(Long canvasId); 
}
