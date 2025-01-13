package com.moa.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.moa.entity.Funding;
import com.moa.entity.Reward;

import io.lettuce.core.dynamic.annotation.Param;

public interface RewardRepository extends JpaRepository<Reward, Long> {
	List<Reward> findByFunding(Funding funding);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT r FROM Reward r where r.rewardId = :rewardId")
	Optional<Reward> findByIdWithLock(@Param("rewardId") Long rewardId);

}
