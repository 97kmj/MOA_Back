package com.moa.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.moa.user.dto.MainArtworkDto;
import com.moa.user.dto.MainFundingDto;
import com.moa.user.repository.MainArtworkRepository;
import com.moa.user.repository.MainFundingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainPageServiceImpl implements MainPageService {
	private final MainArtworkRepository mainArtworkRepository;
	private final MainFundingRepository mainFundingRepository;
	
	@Override
	public Map<String, Object> getMain() throws Exception {
		//메인화면 이미지 리스트 가져오기
		List<MainArtworkDto> artworkList = getArtworkList();
		//펀딩 가져오기
		List<MainFundingDto> fundingList = getMainFundingList();
		
		//response map 리턴
		Map<String,Object> response = new HashMap<>();
		response.put("artworkList", artworkList);
		response.put("fundingList", fundingList);
		return response;
			
	}
	
	
	@Override
	public List<MainArtworkDto> getArtworkList() throws Exception {
		return mainArtworkRepository.getRandomArtwork().stream().map(a->MainArtworkDto.fromEntity(a)).collect(Collectors.toList());
	}

	@Override
	public List<MainFundingDto> getMainFundingList() throws Exception {
		return mainFundingRepository.get4FundingList().stream().map(f->MainFundingDto.fromEntity(f)).collect(Collectors.toList());
	}

}
