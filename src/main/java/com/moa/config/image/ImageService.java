package com.moa.config.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
	String saveImage(String folderName, String fileType, MultipartFile file);
}
