package com.vietlocal.app.service;

import com.vietlocal.app.config.AppProperties;
import com.vietlocal.app.domain.entity.AiChatLog;
import com.vietlocal.app.dto.request.AiChatRequest;
import com.vietlocal.app.dto.response.AiChatResponse;
import com.vietlocal.app.repository.AiChatLogRepository;
import com.vietlocal.app.service.ai.AiProvider;
import com.vietlocal.app.service.ai.DestinationMatcher;
import com.vietlocal.app.service.ai.GeminiAiProvider;
import com.vietlocal.app.service.ai.StubAiProvider;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AiChatService {

	private final AppProperties appProperties;
	private final StubAiProvider stubAiProvider;
	private final GeminiAiProvider geminiAiProvider;
	private final AiChatLogRepository aiChatLogRepository;
	private final DestinationMatcher destinationMatcher;

	@Transactional
	public AiChatResponse chat(AiChatRequest request) {
		String sessionId = request.getSessionId() != null && !request.getSessionId().isBlank()
				? request.getSessionId()
				: UUID.randomUUID().toString();

		AiProvider provider = resolveProvider();
		AiChatResponse response = enrichWithDestination(
				provider.chat(sessionId, request.getMessage()), request.getMessage());

		String guideIds = response.getSuggestedGuideIds() == null
				? null
				: response.getSuggestedGuideIds().stream()
						.map(String::valueOf)
						.collect(Collectors.joining(","));

		aiChatLogRepository.save(AiChatLog.builder()
				.sessionId(response.getSessionId())
				.userMessage(request.getMessage())
				.aiReply(response.getReply())
				.suggestedItinerary(response.getSuggestedItinerary())
				.suggestedGuideIds(guideIds)
				.createdAt(Instant.now())
				.build());

		return response;
	}

	private AiChatResponse enrichWithDestination(AiChatResponse response, String message) {
		return destinationMatcher.match(message).map(d -> {
			if (response.getDetectedDestinationId() != null) {
				return response;
			}
			return response.toBuilder()
					.detectedDestinationId(d.getId())
					.detectedDestinationName(d.getName())
					.detectedDestinationImageUrl(d.getImageUrl())
					.build();
		}).orElse(response);
	}

	private AiProvider resolveProvider() {
		String provider = appProperties.getAi().getProvider();
		if ("gemini".equalsIgnoreCase(provider) && geminiAiProvider.isConfigured()) {
			return geminiAiProvider;
		}
		return stubAiProvider;
	}
}
