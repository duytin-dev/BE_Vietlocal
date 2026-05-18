package com.vietlocal.app.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DestinationTripResponse {
	private Long id;
	private Long destinationId;
	private String destinationName;
	private String destinationSlug;
	private String title;
	private String summary;
	private int durationDays;
}
