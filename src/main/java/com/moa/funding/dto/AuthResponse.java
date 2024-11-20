package com.moa.funding.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {

	@JsonProperty("response")
	private TokenInfo response;

	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class TokenInfo {
		@JsonProperty("access_token")
		private String accessToken;
	}

}