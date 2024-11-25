package com.moa.mypage.artist.service;

import org.springframework.stereotype.Service;

import com.moa.config.image.FolderConstants;
import com.moa.config.image.ImageService;
import com.moa.entity.Funding;
import com.moa.funding.mapper.FundingCreationMapper;
import com.moa.mypage.artist.dto.RegistArtistDto;
import com.moa.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageArtistServiceImpl implements MyPageArtistService {
	private final ImageService imageService;
	private final UserRepository userRepository;
	
	@Override
	public void registArtist(RegistArtistDto registArtistDto) throws Exception {
		String rootFolder = FolderConstants.USER_ROOT; // "user"
		String profileImageType = FolderConstants.USER_PROFILE; // "profileImage"
		String portfolioType = FolderConstants.USER_PORTFOLIO; // "portfolio"
		String profileImageUrl = null;
		String portfolioUrl = null;
		// 이미지 파일 저장 및 정보 설정
		if (registArtistDto.getProfileImage() != null && !registArtistDto.getProfileImage().isEmpty()) {
			profileImageUrl = imageService.saveImage(rootFolder,profileImageType, registArtistDto.getProfileImage());
		} 

//		Funding funding = FundingCreationMapper.toFundingEntity(fundingInfoDTO, fundingMainImageUrl);
//		fundingRepository.save(funding);
//		return funding;
	}

	@Override
	public void modifyArtistInfo() throws Exception {
		// TODO Auto-generated method stub

	}

}
