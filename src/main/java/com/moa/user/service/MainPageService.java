package com.moa.user.service;

import java.util.List;
import java.util.Map;

import com.moa.user.dto.MainArtworkDto;
import com.moa.user.dto.MainFundingDto;

public interface MainPageService {
	
	//메인화면 로드
	Map<String,Object> getMain() throws Exception;
	//메인화면 작품 가져오기
	List<MainArtworkDto> getArtworkList() throws Exception;
	//메인화면 펀딩 가져오기
	List<MainFundingDto> getMainFundingList() throws Exception;
}
