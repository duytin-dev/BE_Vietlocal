package com.vietlocal.app.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DestinationResponse {
	private Long id;
	private String name;
	private String slug;
	private String region;
	private String summary;
	private String description;
	private String imageUrl;
	private boolean featured;
}
