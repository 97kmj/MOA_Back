package com.moa.mypage.artist.service;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.moa.config.image.FolderConstants;
import com.moa.config.image.ImageService;
import com.moa.entity.User;
import com.moa.entity.User.ApprovalStatus;
import com.moa.mypage.artist.dto.EditArtistDto;
import com.moa.mypage.artist.dto.RegistArtistDto;
import com.moa.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageArtistServiceImpl implements MyPageArtistService {
	private final ImageService imageService;
	private final UserRepository userRepository;
	
	@Override
	public void registArtist(RegistArtistDto registArtistDto, MultipartFile portfolio, MultipartFile profileImage) throws Exception {
		String rootFolder = FolderConstants.USER_ROOT; // "user"
		String profileImageType = FolderConstants.USER_PROFILE; // "profileImage"
		String portfolioType = FolderConstants.USER_PORTFOLIO; // "portfolio"
		String profileImageUrl = null;
		String portfolioUrl = null;
		// 이미지 파일 저장 및 정보 설정
		if (profileImage!= null && !profileImage.isEmpty()) {
			profileImageUrl = imageService.saveImage(rootFolder,profileImageType, profileImage);
		}
		if (portfolio != null && !portfolio.isEmpty()) {
			portfolioUrl = imageService.saveImage(rootFolder, portfolioType, portfolio);
		}
		
		//신청 정보 등록
		User user = userRepository.findById(registArtistDto.getUsername()).orElseThrow(()->new Exception("username 오류"));
		user.setArtistNote(registArtistDto.getArtistNote());
		user.setArtistCareer(registArtistDto.getArtistCareer());
		user.setProfileImage(profileImageUrl);
		user.setPortfolioUrl(portfolioUrl);
		user.setArtistApprovalStatus(ApprovalStatus.PENDING);
		user.setApplicationDate(new Timestamp(System.currentTimeMillis()));
		
		userRepository.save(user);
	}
	
	@Override
	public EditArtistDto getArtistInfo(String username) throws Exception {
		User user = userRepository.findById(username).orElseThrow(()->new Exception("username 오류"));
		return EditArtistDto.fromEntity(user);
	}

	@Override
	public void modifyArtistInfo(EditArtistDto editArtistDto, MultipartFile profileImage) throws Exception {
		String rootFolder = FolderConstants.USER_ROOT; // "user"
		String profileImageType = FolderConstants.USER_PROFILE; // "profileImage"
		
		User user = userRepository.findById(editArtistDto.getUsername()).orElseThrow(()->new Exception("username 오류"));
		user.setArtistCareer(editArtistDto.getArtistCareer());
		user.setArtistNote(editArtistDto.getArtistNote());
		
		String profileImageUrl = null;
		//프로필 이미지를 변경했을 경우만 변경
		if (profileImage!= null && !profileImage.isEmpty()) {
			profileImageUrl = imageService.saveImage(rootFolder,profileImageType, profileImage);
			user.setProfileImage(profileImageUrl);
		}
		userRepository.save(user);
	}

}
