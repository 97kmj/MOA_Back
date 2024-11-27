package com.moa.funding.repository;

import javax.persistence.EntityManager;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import com.moa.entity.Funding;
import com.moa.funding.dto.funding.FundingDetailDTO;
import com.moa.funding.dto.funding.FundingDetailDTO.ImageDTO;
import com.moa.funding.dto.funding.FundingDetailDTO.RewardDTO;
import com.moa.funding.mapper.FundingDetailMapper;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.moa.entity.QFunding.funding;
import static com.moa.entity.QReward.reward;
import static com.moa.entity.QFundingImage.fundingImage;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class FundingRepositoryCustomImpl implements FundingRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public FundingRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public FundingDetailDTO findFundingDetailById(Long fundingId) {

		// 1. 펀딩 데이터 가져오기
		Funding funding = getFunding(fundingId);

		// 2. 리워드 데이터 가져오기
		List<RewardDTO> rewardDTOList = getRewardDTOs(fundingId);

		// 3. 작품 이미지 데이터 가져오기
		List<ImageDTO> imageDTOList = getImageDTOs(fundingId);


		// 4. DTO 생성 및 반환
		return FundingDetailMapper.toFundingDetailDTO(funding, rewardDTOList, imageDTOList);
	}

	@NotNull
	private Funding getFunding(Long fundingId) {
		Funding fundingEntity = queryFactory
			.selectFrom(funding)
			.where(funding.fundingId.eq(fundingId))
			.fetchOne();

		if (fundingEntity == null) {
			throw new IllegalStateException("존재하지 않는 펀딩입니다.");
		}
		return fundingEntity;
	}

	@NotNull
	private List<RewardDTO> getRewardDTOs(Long fundingId) {
		//2. reward 조회
		return queryFactory
			.selectFrom(reward)
			.where(reward.funding.fundingId.eq(fundingId))
			.fetch()
			.stream()
			.map(FundingDetailMapper::toRewardDTO)
			.collect(Collectors.toList());
	}

	@NotNull
	private List<ImageDTO> getImageDTOs(Long fundingId) {
		return queryFactory
			.selectFrom(fundingImage)
			.where(fundingImage.funding.fundingId.eq(fundingId))
			.fetch()
			.stream()
			.map(FundingDetailMapper::toImageDTO)
			.collect(Collectors.toList());
	}



	// 조인으로 하는 메서드 나중에 테스트 해보기
	// @Override
	// public FundingDetailDTO findFundingDetailById(Long fundingId) {
	// 	// 1. 조인된 데이터를 조회
	// 	List<Tuple> joinedData = selectJoinedFundingData(fundingId);
	//
	// 	// 2. Funding 엔티티 추출
	// 	Funding fundingEntity = getFundingFromJoinedData(joinedData);
	//
	// 	// 3. RewardDTO 리스트 추출
	// 	List<RewardDTO> rewardDTOList = getRewardDTOListFromJoinedData(joinedData);
	//
	// 	// 4. ImageDTO 리스트 추출
	// 	List<ImageDTO> imageDTOList = getImageDTOListFromJoinedData(joinedData);
	//
	// 	// 5. DTO 생성
	// 	return FundingDetailMapper.toFundingDetailDTO(fundingEntity, rewardDTOList, imageDTOList);
	//
	//
	// }



	// 1. 조인된 데이터를 조회하는 메서드
	private List<Tuple> selectJoinedFundingData(Long fundingId) {
		return queryFactory
			.select(funding, reward, fundingImage)
			.from(funding)
			.leftJoin(reward).on(reward.funding.fundingId.eq(funding.fundingId))
			.leftJoin(fundingImage).on(fundingImage.funding.fundingId.eq(funding.fundingId))
			.where(funding.fundingId.eq(fundingId))
			.fetch();
	}

	// 2. Funding 엔티티 추출
	private Funding getFundingFromJoinedData(List<Tuple> joinedData) {
		return joinedData.get(0).get(funding); // 첫 번째 Tuple에서 Funding 엔티티 추출
	}

	// 3. RewardDTO 리스트 추출
	private List<RewardDTO> getRewardDTOListFromJoinedData(List<Tuple> joinedData) {
		return joinedData.stream()
			.map(tuple -> tuple.get(reward)) // Reward 엔티티 추출
			.filter(Objects::nonNull) // Null 값 제거
			.distinct() // 중복 제거
			.map(FundingDetailMapper::toRewardDTO) // DTO로 변환
			.collect(Collectors.toList());
	}

	// 4. ImageDTO 리스트 추출
	private List<ImageDTO> getImageDTOListFromJoinedData(List<Tuple> joinedData) {
		return joinedData.stream()
			.map(tuple -> tuple.get(fundingImage)) // FundingImage 엔티티 추출
			.filter(Objects::nonNull) // Null 값 제거
			.distinct() // 중복 제거
			.map(FundingDetailMapper::toImageDTO) // DTO로 변환
			.collect(Collectors.toList());
	}




}
