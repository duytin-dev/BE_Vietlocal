package com.vietlocal.app.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BrandStoryResponse {
	private Long id;
	private String title;
	private String content;
	private String heroImageUrl;
	private String tagline;
}
