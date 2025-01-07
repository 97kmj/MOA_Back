package com.moa.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.moa.funding.dto.payment.RewardRequest;

@Configuration
public class RedisConfig {

	@Bean
	public RedisTemplate<String, List<RewardRequest>> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, List<RewardRequest>> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);

		// Key와 Value 직렬화 설정
		template.setKeySerializer(new StringRedisSerializer()); // Key는 String으로 직렬화
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // Value는 JSON으로 직렬화

		return template;
	}

	@Bean
	public RedisTemplate<String, Integer> userLimitRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Integer> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);

		// Key와 Value 직렬화 설정
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericToStringSerializer<>(Integer.class));

		return template;
	}

}
