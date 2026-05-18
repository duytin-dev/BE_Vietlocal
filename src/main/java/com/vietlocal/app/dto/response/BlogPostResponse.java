package com.vietlocal.app.dto.response;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BlogPostResponse {
	private Long id;
	private String title;
	private String slug;
	private String excerpt;
	private String content;
	private String coverImageUrl;
	private Long destinationId;
	private String destinationSlug;
	private String destinationName;
	private Instant publishedAt;
}
