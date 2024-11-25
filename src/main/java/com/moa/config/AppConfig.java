package com.moa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.moa.config.image.ImageService;
import com.moa.config.image.ImageServiceImpl;

@Configuration
public class AppConfig {

	@Bean
	public ImageService imageService() {
		return new ImageServiceImpl();
	}
}