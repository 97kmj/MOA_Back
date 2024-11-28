package com.moa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.Funding;
import com.moa.entity.Reward;

public interface RewardRepository extends JpaRepository<Reward, Long> {
	List<Reward> findByFunding(Funding funding);
}
