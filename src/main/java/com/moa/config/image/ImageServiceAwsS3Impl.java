package com.moa.config.image;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//
// @Service
// @Primary
public class ImageServiceAwsS3Impl implements ImageService {

	private final S3Client s3Client;
	private final String bucketName;
	private final String region; // region 필드 추가

	public ImageServiceAwsS3Impl(S3Client s3Client, String bucketName, String region) {
		this.s3Client = s3Client;
		this.bucketName = bucketName;
		this.region = region;
	}


	// // 생성자
	// public ImageServiceAwsS3Impl(
	// 	@Value("${aws.s3.bucket-name}") String bucketName,
	// 	@Value("${aws.s3.access-key}") String accessKey,
	// 	@Value("${aws.s3.secret-key}") String secretKey,
	// 	@Value("${aws.s3.region}") String region
	// ) {
	// 	this.bucketName = bucketName;
	// 	this.region = region; // region 초기화
	//
	// 	// S3Client 초기화
	// 	this.s3Client = S3Client.builder()
	// 		.region(Region.of(region))
	// 		.credentialsProvider(StaticCredentialsProvider.create(
	// 			AwsBasicCredentials.create(accessKey, secretKey)
	// 		))
	// 		.build();
	// }

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
			String uniqueFileName = folderName + "/" + fileType + "/" + UUID.randomUUID() + extension;

			// S3 업로드 요청
			PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(uniqueFileName)
				.contentType(file.getContentType())
				// .acl("public-read") // 퍼블릭 읽기 권한 부여
				.build();

			s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

			// 업로드된 파일 URL 반환
			return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + uniqueFileName;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("S3에 이미지 업로드 중 오류 발생", e);
		}
	}
}
