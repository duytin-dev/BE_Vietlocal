package com.vietlocal.app.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class AiChatResponse {
	private String sessionId;
	private String reply;
	private String suggestedItinerary;
	private List<Long> suggestedGuideIds;
	/** gemini | stub */
	private String provider;
	private Long detectedDestinationId;
	private String detectedDestinationName;
	private String detectedDestinationImageUrl;
}
