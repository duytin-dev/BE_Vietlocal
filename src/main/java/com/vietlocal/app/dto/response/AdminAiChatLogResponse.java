package com.vietlocal.app.dto.response;

import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminAiChatLogResponse {
	private Long id;
	private String sessionId;
	private String userMessage;
	private String aiReply;
	private String suggestedItinerary;
	private List<Long> suggestedGuideIds;
	private Instant createdAt;
}
