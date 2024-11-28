package com.moa.funding.repository;

import javax.persistence.EntityManager;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.moa.entity.Funding;
import com.moa.entity.QFunding;
import com.moa.funding.dto.funding.FundingDetailDTO;
import com.moa.funding.dto.funding.FundingDetailDTO.ImageDTO;
import com.moa.funding.dto.funding.FundingDetailDTO.RewardDTO;
import com.moa.funding.dto.funding.FundingListDTO;
import com.moa.funding.dto.funding.FundingResponse;
import com.moa.funding.mapper.FundingDetailMapper;
import com.moa.funding.mapper.FundingMapper;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.moa.entity.QFunding.funding;
import static com.moa.entity.QReward.reward;
import static com.moa.entity.QFundingImage.fundingImage;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	@Override
	public FundingResponse findFundingList(String filterType, String sortOption, int page) {
		QFunding funding = QFunding.funding;

		int pageSize = 8;
		int offset = page * pageSize;

		//필터 조건과 정렬 조건 생성 
		BooleanExpression filterCondition = getFilterCondition(filterType);
		OrderSpecifier<?> orderSpecifier = getSortCondition(filterType, sortOption);

		List<FundingListDTO> fundingList = queryFactory.selectFrom(funding)
			.where(filterCondition)
			.orderBy(orderSpecifier)
			.offset(offset)
			.limit(pageSize)
			.fetch()
			.stream()
			.map(FundingMapper::toFundingListDTO)
			.collect(Collectors.toList());

		// 전체 데이터 개수 계산 (fetchCount() 대신 서브쿼리 사용)
		Long totalCount = Optional.ofNullable(
			queryFactory.select(funding.count())
				.from(funding)
				.where(filterCondition)
				.fetchOne()
		).orElse(0L); // null일 경우 0 반환

		boolean isLastPage = (offset + pageSize) >= totalCount;

		log.info("isLastPage: {}", isLastPage);

		return FundingResponse.builder()
			.fundingList(fundingList)
			.isLastPage(isLastPage)
			.build();
	}

	@Override
	@Transactional
	public void updateFundingToOnGoing() {
		QFunding funding = QFunding.funding;

		//오늘 날짜를 구함
		LocalDate today = LocalDate.now();
		//오늘 자정을 구함 2024-11-28 00:00 (한국) = 2024-11-27 15:00 (UTC)
		Instant startOfToday = today.atStartOfDay(ZoneId.systemDefault()).toInstant();

		//오늘에서 하루를 더해서 내일 자정을 구함 그리고 -1초를 해서 오늘의 마지막 시간 23:59:59를 구했당~
		Instant endOfToday = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

		long updatedCount = queryFactory.update(funding)
			.set(funding.fundingStatus, Funding.FundingStatus.ONGOING)
			.where(funding.approvalStatus.eq(Funding.ApprovalStatus.APPROVED)
				.and(funding.fundingStatus.eq(Funding.FundingStatus.STANDBY))
					.and(funding.startDate.between(startOfToday, endOfToday))
				// .and(funding.startDate.goe(startOfToday)) // startDate >= 오늘 자정
				// .and(funding.startDate.lt(endOfToday))    // startDate < 내일 자정
			)
			.execute();

		log.info("updateFundingToOnGoing updatedCount: {}", updatedCount);
	}

	@Override
	@Transactional
	public void updateFundingToSuccessful() {
		QFunding funding = QFunding.funding;

		Instant today = Instant.now();

		long updateCount = queryFactory.update(funding)
			.set(funding.fundingStatus, Funding.FundingStatus.SUCCESSFUL)
			.where (funding.approvalStatus.eq(Funding.ApprovalStatus.APPROVED)
				.and(funding.fundingStatus.eq(Funding.FundingStatus.ONGOING)
				.and(funding.currentAmount.goe(funding.goalAmount)))
				.and(funding.endDate.goe(today)) //endDate 날짜 >= 오늘  오늘보다 크거나 같은 경우
			)
				.execute();

		log.info("updateFundingToSuccessful updateCount: {}", updateCount);
	}

	@Override
	@Transactional
	public void updateFundingToFailed(){
	QFunding funding = QFunding.funding;

	Instant today = Instant.now();

		long updatedCount = queryFactory.update(funding)
			.set(funding.fundingStatus, Funding.FundingStatus.FAILED)
			.where(funding.approvalStatus.eq(Funding.ApprovalStatus.APPROVED)
				.and(funding.fundingStatus.eq(Funding.FundingStatus.ONGOING))
				.and(funding.endDate.before(today)) //endDate 날짜 < 오늘
				.and(funding.currentAmount.lt(funding.goalAmount)) // 현재금액 < 목표금액 :현재 금액이 목표금액보다 작은 경우
			)
			.execute();

		log.info("updateFundingToFailed updatedCount: {}", updatedCount);
	}


	private BooleanExpression getFilterCondition(String filterType) {
		QFunding funding = QFunding.funding;

		Instant today = Instant.now();//오늘
		Instant endRange = today.plus(7, ChronoUnit.DAYS); // 오늘포함 최대 7일 후

		switch (filterType) {
			case "공개 예정 펀딩":
				return funding.fundingStatus.eq(Funding.FundingStatus.STANDBY)
					.and(funding.approvalStatus.eq(Funding.ApprovalStatus.APPROVED))
					.and(funding.startDate.gt(today))//오늘 이후
					.and(funding.startDate.loe(endRange));//7일 이내

			case "진행중 펀딩":
				return funding.approvalStatus.eq(Funding.ApprovalStatus.APPROVED)
					.and(
						funding.fundingStatus.eq(Funding.FundingStatus.ONGOING)
							.or(
								funding.fundingStatus.eq(Funding.FundingStatus.SUCCESSFUL)
									.and(funding.endDate.goe(today)) // 성공 펀딩 중 마감 기간 남음
							)
					);

			case "마감된 펀딩":
				return funding.fundingStatus.eq(Funding.FundingStatus.SUCCESSFUL)
					.and(funding.approvalStatus.eq(Funding.ApprovalStatus.APPROVED))
					.and(funding.endDate.lt(today)); // 성공 펀딩이지만 이미 마감된 경우

			default:
				throw new IllegalArgumentException("Invalid filter type: " + filterType);
		}

	}

	private OrderSpecifier<?> getSortCondition(String filterType, String sortOption) {
		QFunding funding = QFunding.funding;
		switch (filterType) {
			case "공개 예정 펀딩":
				if ("가까운순".equals(sortOption)) {
					return funding.startDate.asc();
				} else if ("멀리있는순".equals(sortOption)) {
					return funding.startDate.desc();
				}
				break;

			case "진행중 펀딩":
				if ("최신순".equals(sortOption)) {
					return funding.applicationDate.desc(); // 최신순: 생성일 기준 내림차순
				} else if ("마감 임박순".equals(sortOption)) {
					return funding.endDate.asc(); // 마감 임박순
				} else if ("오래된순".equals(sortOption)) {
					return funding.applicationDate.asc(); // 오래된순: 생성일 기준 오름차순
				} else if ("달성률 높은순".equals(sortOption)) {
					return funding.currentAmount.divide(funding.goalAmount).desc(); // 달성률 높은순
				} else if ("달성률 낮은순".equals(sortOption)) {
					return funding.currentAmount.divide(funding.goalAmount).asc(); // 달성률 낮은순
				} else if ("목표금액 높은순".equals(sortOption)) {
					return funding.goalAmount.desc(); // 목표금액 높은순
				} else if ("목표금액 낮은순".equals(sortOption)) {
					return funding.goalAmount.asc(); // 목표금액 낮은순
				}

				break;

			case "마감된 펀딩":
				if ("최신순".equals(sortOption)) {
					return funding.applicationDate.desc();
				} else if ("오래된순".equals(sortOption)) {
					return funding.applicationDate.asc();
				}
				break;

			default:
				throw new IllegalArgumentException("Invalid sort option: " + sortOption);

		}
		return funding.applicationDate.desc();
	}

}
