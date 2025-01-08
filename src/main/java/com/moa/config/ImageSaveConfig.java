package com.moa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.moa.config.image.ImageService;
import com.moa.config.image.ImageServiceAwsS3Impl;
import com.moa.config.image.ImageServiceImpl;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class ImageSaveConfig {

	@Bean
	public S3Client s3Client(
		@Value("${aws.s3.access-key}") String accessKey,
		@Value("${aws.s3.secret-key}") String secretKey,
		@Value("${aws.s3.region}") String region
	) {
		return S3Client.builder()
			.region(Region.of(region))
			.credentialsProvider(
				StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))
			)
			.build();
	}

	@Bean
	@Primary
	public ImageService imageService(S3Client s3Client,
		@Value("${aws.s3.bucket-name}") String bucketName,
		@Value("${aws.s3.region}") String region) {
		return new ImageServiceAwsS3Impl(s3Client, bucketName, region);
	}

	@Bean
	public ImageService imageService() {
		return new ImageServiceImpl();
	}

}