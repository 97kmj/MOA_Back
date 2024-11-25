package com.moa.config.image;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

public class ImageServiceImpl implements ImageService {

	private final RestTemplate restTemplate = new RestTemplate();
	// private final String basePath = "http://16.171.159.198:8000"; //aws
	private final String basePath = "http://192.168.0.248:8000"; //로컬


	// 이미지 저장 로직
	@Override
	public String saveImage(String folderName, String fileType, MultipartFile file) {
		try {

			// 원래 파일 이름에서 확장자 추출
			String originalFileName = file.getOriginalFilename();
			String extension = "";

			if (originalFileName != null && originalFileName.contains(".")) {
				extension = originalFileName.substring(originalFileName.lastIndexOf("."));
			}

			// 고유한 파일 이름 생성 (UUID + 확장자)
			String uniqueFileName = UUID.randomUUID().toString() + extension;

			// Flask 서버의 업로드 URL
			String serverUrl = basePath + "/upload";

			// HTTP 헤더 설정
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			// MultiValueMap 생성하여 파일과 타입을 추가
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("fileType", fileType);
			body.add("fileName", uniqueFileName);

			// 파일 데이터를 ByteArrayResource로 추가
			ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
				@Override
				public String getFilename() {
					return uniqueFileName; // 고유 파일 이름 반환
				}
			};
			body.add(fileType, fileResource);

			// HTTP 요청 엔티티 생성
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			// Flask 서버로 파일 업로드 요청
			ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, requestEntity, String.class);

			// 업로드 성공 여부 확인
			if (response.getStatusCode().is2xxSuccessful()) {
				// return uniqueFileName; // 성공 시 고유 파일 이름 반환
				return basePath + "/" + folderName + "/" +fileType + "/" + uniqueFileName;
			} else {
				throw new RuntimeException("이미지 업로드 실패");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("이미지 업로드 중 오류 발생");
		}
	}


	public String generateImageUrl(String fileType, String filename) {
		String folder;

		switch (fileType) {
			case "saleImage":
				folder = "artWork/sale";
				break;
			case "mainImage":
				folder = "funding/mainImg";
				break;
			case "artImage":
				folder = "funding/artImg";
				break;
			default:
				folder = "defaultFolder";
				break;
		}
		return basePath + "/" + folder + "/" + filename;
	}

	// 이미지 한장조회
	public byte[] getImage(String filename, String type) {
		try {
			// 타입에 따라 폴더명 결정
			String folder;
			switch (type) {
				case "saleImage":
					folder = "artWork/sale";
					break;
				case "mainImage":
					folder = "funding/mainImg";
					break;
				case "artImage":
					folder = "funding/artImg";
					break;
				default:
					folder = "defaultFolder"; // 기본 폴더 설정
					break;
			}

			// 이미지 URL 생성
			String imageUrl = basePath + "/" + folder + "/" + filename;
			ResponseEntity<byte[]> response = restTemplate.getForEntity(imageUrl, byte[].class);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 여러 이미지 조회
	public List<byte[]> getImages(List<String> filenames, String folder) {
		List<byte[]> images = new ArrayList<>();
		for (String filename : filenames) {
			images.add(getImage(filename, folder));
		}
		return images;
	}

}