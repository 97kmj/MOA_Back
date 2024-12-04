package com.moa.admin.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.moa.admin.dto.AdminOrderItemDto;
import com.moa.admin.dto.ArtistUserDto;
import com.moa.admin.dto.BlackArtworkDto;
import com.moa.admin.dto.FrameDto;
import com.moa.admin.dto.FundingApplyDto;
import com.moa.admin.dto.FundingRewardDto;
import com.moa.admin.dto.NoticeDto;
import com.moa.admin.dto.QuestionDto;
import com.moa.admin.dto.RegistFrameDto;
import com.moa.admin.dto.RegistNoticeDto;
import com.moa.admin.dto.UpdateOrderItemStatusRequest;
import com.moa.admin.repository.AdminFundingRepository;
import com.moa.admin.repository.AdminItemRepository;
import com.moa.admin.repository.AdminQnARepository;
import com.moa.entity.Artwork;
import com.moa.entity.Artwork.SaleStatus;
import com.moa.entity.Canvas;
import com.moa.entity.FrameOption;
import com.moa.entity.Funding;
import com.moa.entity.Notice;
import com.moa.entity.OrderItem;
import com.moa.entity.OrderItem.ShippingStatus;
import com.moa.entity.Question;
import com.moa.entity.User;
import com.moa.entity.User.ApprovalStatus;
import com.moa.entity.User.Role;
import com.moa.repository.ArtworkRepository;
import com.moa.repository.CanvasRepository;
import com.moa.repository.FrameOptionRepository;
import com.moa.repository.FundingRepository;
import com.moa.repository.NoticeRepository;
import com.moa.repository.OrderItemRepository;
import com.moa.repository.OrderRepository;
import com.moa.repository.QuestionRepository;
import com.moa.repository.RewardRepository;
import com.moa.repository.UserRepository;
import com.moa.shop.dto.CanvasDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final NoticeRepository noticeRepository;
	private final QuestionRepository questionRepository;
	private final AdminQnARepository adminQnARepository;
	private final UserRepository userRepository;
	private final FundingRepository fundingRepository;
	private final AdminFundingRepository adminFundingRepository;
	private final RewardRepository rewardRepository;
	private final FrameOptionRepository frameOptionRepository;
	private final CanvasRepository canvasRepository;
	private final ArtworkRepository artworkRepository;
	private final AdminItemRepository adminItemRepository;
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository; 
	//admin notice 
	@Override
	public List<NoticeDto> allNoticeList() throws Exception {
		List<NoticeDto> noticeList = null;
		noticeList = noticeRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream().map(notice->NoticeDto.fromEntity(notice)).collect(Collectors.toList());
		return noticeList;
	}
	
	@Transactional
	@Override
	public NoticeDto modifyNotice(RegistNoticeDto noticeDto) throws Exception {
		Notice notice = noticeRepository.findById(noticeDto.getNoticeId()).orElseThrow(()->new Exception("공지사항 수정 오류"));
		notice.setTitle(noticeDto.getTitle());
		notice.setContent(noticeDto.getContent());
		Notice modifiedNotice = noticeRepository.save(notice);
		return NoticeDto.fromEntity(modifiedNotice);
	}
	@Transactional
	@Override
	public NoticeDto registNotice(RegistNoticeDto noticeDto) throws Exception {
		Notice notice = Notice.builder().title(noticeDto.getTitle()).content(noticeDto.getContent()).build();
		Notice newNotice = noticeRepository.save(notice);
		return NoticeDto.fromEntity(newNotice);
	}
	
	@Override
	public void deleteNotice(Long noticeId) throws Exception {
		noticeRepository.deleteById(noticeId);
	}
	
	//admin QnA
	@Override
	public List<QuestionDto> notAnswerQuestionList() throws Exception {	
		return questionRepository.findByAnswerStatusOrderByQuestionAtDesc(false).stream().map(q->QuestionDto.fromEntity(q)).collect(Collectors.toList());
	}
	@Override
	public List<QuestionDto> answeredQuestionList(Date startDate,Date endDate) throws Exception {
		return adminQnARepository.findAnsweredQuestionByDate(startDate, endDate).stream().map(q->QuestionDto.fromEntity(q)).collect(Collectors.toList());
	}
	
	//writeAnswer
	@Override
	public void writeAnswer(QuestionDto questionDto) throws Exception {
		Question question = questionRepository.findById(questionDto.getQuestionId()).orElseThrow(()->new Exception("questionID 오류"));
		question.setAnswerAt(questionDto.getAnswerAt());
		question.setAnswerTitle(questionDto.getAnswerTitle());
		question.setAnswerContent(questionDto.getAnswerContent());
		question.setAnswerStatus(questionDto.getAnswerStatus());
		questionRepository.save(question);
	}
	
	
	
	//관리자 작가 신청목록 불러오기 
	@Override
	public List<ArtistUserDto> getApplyArtistList() throws Exception {
		List<ArtistUserDto> applyArtistList = userRepository.findByArtistApprovalStatus(ApprovalStatus.PENDING).stream().map((u)->ArtistUserDto.fromEntity(u)).collect(Collectors.toList());
		return applyArtistList;
	}
	
	@Override
	public void approveArtist(String username) throws Exception {
		User user = userRepository.findById(username).orElseThrow(()->new Exception("username오류"));
		user.setArtistApprovalStatus(ApprovalStatus.APPROVED);
		user.setRole(Role.ARTIST);
		userRepository.save(user);
		
	}
	
	@Override
	public void rejectArtist(String username) throws Exception {
		User user = userRepository.findById(username).orElseThrow(()->new Exception("username오류"));
		user.setArtistApprovalStatus(ApprovalStatus.NORMAL);
		userRepository.save(user);	
	}
	
	//관리자 펀딩 신청 리스트 불러오기
	@Override
	public List<FundingApplyDto> getApplyFundingList() throws Exception {
		List<Funding> pendingList = fundingRepository.findByApprovalStatus(Funding.ApprovalStatus.PENDING); //승인대기중인 리스트
		List<FundingApplyDto> applyFundingList = new ArrayList<>();
				
		for(Funding funding : pendingList) {
			Long fundingId = funding.getFundingId();
			FundingApplyDto fundingApplyDto = FundingApplyDto.fromFunding(funding);
			List<FundingRewardDto> rewardList = rewardRepository.findByFunding(funding).stream().map(r->FundingRewardDto.fromReward(r)).collect(Collectors.toList());
			fundingApplyDto.setRewardList(rewardList);
			fundingApplyDto.setImageUrlList(adminFundingRepository.getFundingImageUrlByFundingId(fundingId));
			applyFundingList.add(fundingApplyDto);
		}
		return applyFundingList;
	}
	
	//펀딩 승인
	@Override
	public void approveFunding(Long fundingId) throws Exception {
		Funding funding = fundingRepository.findById(fundingId).orElseThrow(()->new Exception("fundingId 오류"));
		funding.setApprovalStatus(Funding.ApprovalStatus.APPROVED);
		fundingRepository.save(funding);
	}
	//펀딩 반려
	@Override
	public void rejectFunding(Long fundingId) throws Exception {
		Funding funding = fundingRepository.findById(fundingId).orElseThrow(()->new Exception("fundingId 오류"));
		funding.setApprovalStatus(Funding.ApprovalStatus.REJECTED);
		fundingRepository.save(funding);		
	}
	
	//등록된 프레임옵션 가져오기
	@Override
	public List<FrameDto> getFrameList() throws Exception {
		return frameOptionRepository.findAll().stream().map(f->FrameDto.fromEntity(f)).collect(Collectors.toList());	
	}
	@Override
	public List<CanvasDto> getCanvasList(String canvasType) throws Exception {
		Canvas.CanvasType type = Canvas.CanvasType.valueOf(canvasType);
		return canvasRepository.findByCanvasType(type) 
	            .stream()
	            .map(CanvasDto::fromEntity)
	            .collect(Collectors.toList());
	}
	//새로운 프레임 등록
	@Override
	public FrameDto registFrame(RegistFrameDto registFrameDto) throws Exception {
		FrameOption frame = FrameOption.builder()
									.frameType(registFrameDto.getFrameType())
									.framePrice(registFrameDto.getFramePrice())
									.canvas(canvasRepository.findById(registFrameDto.getCanvasId()).orElseThrow(()->new Exception("canvasId 오류")))
									.stock(registFrameDto.getStock())
									.build();
		frameOptionRepository.save(frame);
		return FrameDto.fromEntity(frame);
	}
	//프레임 가격,재고 수정
	@Override
	public void updateFrame(FrameDto frameDto) throws Exception {
		FrameOption frame = frameOptionRepository.findById(frameDto.getFrameId()).orElseThrow(()->new Exception("frameId 오류"));
		frame.setFramePrice(frameDto.getFramePrice());
		frame.setStock(frameDto.getStock());
		frameOptionRepository.save(frame);
	}
	
	//의심작품 체크
	@Override
	public void changeAdminCheck(Long artworkId, Boolean isSuspicious) throws Exception {
		Artwork artwork = artworkRepository.findById(artworkId).orElseThrow(()->new Exception("artworkId 오류"));
		artwork.setAdminCheck(isSuspicious);
		artworkRepository.save(artwork);
	}
	//의심작품목록 가져오기
	@Override
	public List<BlackArtworkDto> getBlackArtworks() throws Exception {
		return artworkRepository.findByAdminCheck(Boolean.TRUE).stream()
				.map(a->BlackArtworkDto.fromEntity(a))
				.collect(Collectors.toList());
	}
	//의심작품 삭제
	@Override
	public void deleteBlackArtwork(Long artworkId) throws Exception {
		Artwork blackArtwork = artworkRepository.findById(artworkId).orElseThrow(()->new Exception("artworkId 오류"));
		blackArtwork.setSaleStatus(SaleStatus.DELETE);
		blackArtwork.setAdminCheck(Boolean.FALSE);
		artworkRepository.save(blackArtwork);
	}
	
	
	//상품 관리 목록 가져오기
	@Override
	public List<AdminOrderItemDto> getOrderItemList() throws Exception {
		List<OrderItem> orderItemEntity = adminItemRepository.findOrderItemList();
		return orderItemEntity.stream().map(o->AdminOrderItemDto.fromEntity(o)).collect(Collectors.toList());
	}
	
	@Override
	public void updateShippingStatus(List<UpdateOrderItemStatusRequest> updateList) throws Exception {
		for(UpdateOrderItemStatusRequest update : updateList) {
			OrderItem orderItem = orderItemRepository.findById(update.getOrderItemId()).orElseThrow(()->new Exception("수정할 orderItemId 오류"));
			orderItem.setShippingStatus(ShippingStatus.valueOf(update.getStatus()));
			orderItemRepository.save(orderItem);
		}
	}
	
	
}
