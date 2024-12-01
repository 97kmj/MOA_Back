package com.moa.funding.service.fundingImplements;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.moa.entity.Reward;
import com.moa.funding.dto.payment.RewardRequest;
import com.moa.funding.exception.RewardStockException;
import com.moa.funding.service.RewardService;
import com.moa.repository.RewardRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RewardServiceImpl implements RewardService {
	private final RewardRepository rewardRepository;

	@Override
	public void reduceRewardStock(RewardRequest rewardRequest) {
		// Reward 조회
		Reward reward = getReward(rewardRequest);

		// BASIC 리워드 처리 여부 확인
		if (isBasicReward(reward)) {
			return; // BASIC 리워드는 작업 건너뜀
		}
		// 재고 검증
		validateRewardStock(reward, rewardRequest.getRewardQuantity());
		// 재고 감소
		reward.setStock(reward.getStock() - rewardRequest.getRewardQuantity().intValue());
		rewardRepository.save(reward);
	}

	@Override
	public Reward getReward(RewardRequest rewardRequest) {
		return rewardRepository.findById(rewardRequest.getRewardId())
			.orElseThrow(() -> new IllegalArgumentException("리워드가 존재하지 않습니다."));
	}

	@Override
	public void restoreRewardStock(RewardRequest rewardRequest) {
		Reward reward = getReward(rewardRequest);

		// BASIC 리워드 처리 여부 확인
		if (isBasicReward(reward)) {
			return; // BASIC 리워드는 작업 건너뜀
		}
		// 재고 복구  // 밑에 코드 정리하기  비교해서
		// 기존 재고가 없으면 요청한 수량으로 설정
		// if (reward.getStock() == null) {
		// 	reward.setStock(rewardRequest.getRewardQuantity().intValue());
		// } else { // 기존 재고가 있으면 요청한 수량을 추가
		// 	reward.setStock(reward.getStock() + rewardRequest.getRewardQuantity().intValue());
		// }

		reward.setStock(Optional.ofNullable(reward.getStock()).orElse(0) + rewardRequest.getRewardQuantity().intValue());

		rewardRepository.save(reward);
	}

	@Override
	public void validateRewardStock(RewardRequest rewardRequest) {
		// Reward 조회
		Reward reward = getReward(rewardRequest);
		// 재고 검증
		validateRewardStock(reward, rewardRequest.getRewardQuantity());
	}

	private void validateRewardStock(Reward reward, Long requestedQuantity) {

		if (reward.getRewardType() == Reward.RewardType.BASIC) {
			log.info("기본 리워드(BASIC)는 재고 검사를 건너뜀 - RewardId: {}", reward.getRewardId());
			return;
		}

		if (reward.getStock() < requestedQuantity) {
			throw new RewardStockException("리워드 재고가 부족합니다. - 리워드명: " + reward.getRewardName());
		}
	}


	private boolean isBasicReward(Reward reward) {
		if (reward.getRewardType() == Reward.RewardType.BASIC) {
			log.info("기본 리워드(BASIC)는 작업을 건너뜀 - RewardId: {}", reward.getRewardId());
			return true;
		}
		return false;
	}

}
