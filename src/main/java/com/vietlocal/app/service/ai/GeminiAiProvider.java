package com.vietlocal.app.service.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vietlocal.app.config.AppProperties;
import com.vietlocal.app.domain.entity.Destination;
import com.vietlocal.app.domain.entity.Guide;
import com.vietlocal.app.dto.response.AiChatResponse;
import com.vietlocal.app.repository.DestinationRepository;
import com.vietlocal.app.repository.GuideRepository;
import com.vietlocal.app.service.ai.gemini.GeminiApiModels;
import com.vietlocal.app.service.ai.gemini.GeminiApiModels.Content;
import com.vietlocal.app.service.ai.gemini.GeminiApiModels.GenerateContentRequest;
import com.vietlocal.app.service.ai.gemini.GeminiApiModels.GenerationConfig;
import com.vietlocal.app.service.ai.gemini.GeminiApiModels.PlannerPayload;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
@RequiredArgsConstructor
@Slf4j
public class GeminiAiProvider implements AiProvider {

	private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/";

	private final AppProperties appProperties;
	private final DestinationRepository destinationRepository;
	private final GuideRepository guideRepository;
	private final StubAiProvider stubAiProvider;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final RestClient restClient = RestClient.create();

	public boolean isConfigured() {
		String key = appProperties.getAi().getGemini().getApiKey();
		return key != null && !key.isBlank();
	}

	@Override
	public AiChatResponse chat(String sessionId, String message) {
		if (!isConfigured()) {
			log.warn("GEMINI_API_KEY chưa cấu hình — dùng stub demo");
			return stubAiProvider.chat(sessionId, message);
		}

		List<Guide> guides = guideRepository.findAvailableOrderByRatingDesc(PageRequest.of(0, 10));
		List<Destination> destinations = destinationRepository.findAll();
		GenerateContentRequest body = new GenerateContentRequest(
				List.of(Content.ofText(message)),
				Content.ofText(buildSystemPrompt(destinations, guides)),
				new GenerationConfig(0.6, 1536, "application/json"));

		RestClientResponseException lastError = null;
		for (String model : modelsToTry()) {
			try {
				log.info("Gemini request model={}", model);
				String rawJson = callGemini(model, body);
				if (rawJson == null || rawJson.isBlank()) {
					continue;
				}
				PlannerPayload payload = objectMapper.readValue(cleanJson(rawJson), PlannerPayload.class);
				List<Long> guideIds = sanitizeGuideIds(payload.suggestedGuideIds(), guides);
				AiChatResponse.AiChatResponseBuilder builder = AiChatResponse.builder()
						.sessionId(sessionId)
						.reply(payload.reply() != null ? payload.reply().trim() : "")
						.suggestedItinerary(
								payload.suggestedItinerary() != null ? payload.suggestedItinerary().trim() : "")
						.suggestedGuideIds(guideIds)
						.provider("gemini");
				resolveDestination(payload.detectedDestinationId(), destinations).ifPresent(d -> builder
						.detectedDestinationId(d.getId())
						.detectedDestinationName(d.getName())
						.detectedDestinationImageUrl(d.getImageUrl()));
				return builder.build();
			} catch (RestClientResponseException e) {
				lastError = e;
				if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
					log.warn("Gemini model {} quota/rate limit (429), trying next model", model);
					continue;
				}
				log.error("Gemini API error {} on {}: {}", e.getStatusCode(), model, e.getResponseBodyAsString());
				break;
			} catch (Exception e) {
				log.error("Gemini failed on model {}", model, e);
				break;
			}
		}

		if (lastError != null) {
			log.error("All Gemini models failed, last status {}", lastError.getStatusCode());
		}
		if (appProperties.getAi().getGemini().isFallbackToStub()) {
			log.warn("Falling back to stub demo provider");
			return stubAiProvider.chat(sessionId, message);
		}
		String hint = lastError != null && lastError.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS
				? "Đã hết quota free tier Google AI. Đợi vài phút, tạo API key mới tại aistudio.google.com/apikey, hoặc bật billing."
				: "Kiểm tra GEMINI_API_KEY và log server.";
		return failureResponse(sessionId, "Không gọi được Gemini. " + hint);
	}

	private List<String> modelsToTry() {
		Set<String> ordered = new LinkedHashSet<>();
		String primary = appProperties.getAi().getGemini().getModel();
		if (primary != null && !primary.isBlank()) {
			ordered.add(primary.trim());
		}
		List<String> fallbacks = appProperties.getAi().getGemini().getFallbackModels();
		if (fallbacks != null) {
			fallbacks.stream().filter(m -> m != null && !m.isBlank()).map(String::trim).forEach(ordered::add);
		}
		if (ordered.isEmpty()) {
			ordered.add("gemini-2.5-flash-lite");
		}
		return List.copyOf(ordered);
	}

	private String callGemini(String model, GenerateContentRequest body) {
		String apiKey = appProperties.getAi().getGemini().getApiKey();
		String url = BASE_URL + model + ":generateContent?key=" + apiKey;
		var response = restClient.post()
				.uri(url)
				.contentType(MediaType.APPLICATION_JSON)
				.body(body)
				.retrieve()
				.body(GeminiApiModels.GenerateContentResponse.class);
		return response != null ? response.firstText() : null;
	}

	private AiChatResponse failureResponse(String sessionId, String userMessage) {
		return AiChatResponse.builder()
				.sessionId(sessionId)
				.reply(userMessage)
				.suggestedItinerary("")
				.suggestedGuideIds(guideRepository.findAvailableOrderByRatingDesc(PageRequest.of(0, 3)).stream()
						.map(Guide::getId)
						.toList())
				.provider("gemini")
				.build();
	}

	private String buildSystemPrompt(List<Destination> destinations, List<Guide> guides) {
		StringBuilder sb = new StringBuilder();
		sb.append("""
				Bạn là trợ lý du lịch VietLocal (Việt Nam). Trả lời tiếng Việt trừ khi user dùng tiếng Anh.
				Chỉ trả JSON hợp lệ (không markdown):
				{"reply":"...","suggestedItinerary":"...","suggestedGuideIds":[1,2,3],"detectedDestinationId":1}
				suggestedItinerary: mỗi ngày một dòng "Ngày N: ...". suggestedGuideIds: tối đa 3 id từ list HDV.
				detectedDestinationId: id điểm đến user nhắc tới (từ list Điểm đến), null nếu không rõ.

				Điểm đến:
				""");
		destinations.stream().limit(10).forEach(d -> sb.append("- id=").append(d.getId()).append(" ")
				.append(d.getName())
				.append(": ").append(d.getSummary() != null ? d.getSummary() : "").append("\n"));
		sb.append("HDV:\n");
		guides.forEach(g -> sb.append("- id=").append(g.getId()).append(" ").append(g.getName())
				.append(" ").append(g.getPricePerDay()).append("đ/ngày\n"));
		return sb.toString();
	}

	private Optional<Destination> resolveDestination(Long id, List<Destination> destinations) {
		if (id == null) {
			return Optional.empty();
		}
		return destinations.stream().filter(d -> id.equals(d.getId())).findFirst();
	}

	private List<Long> sanitizeGuideIds(List<Long> ids, List<Guide> guides) {
		Set<Long> valid = guides.stream().map(Guide::getId).collect(Collectors.toSet());
		List<Long> result = new ArrayList<>();
		if (ids != null) {
			for (Long id : ids) {
				if (id != null && valid.contains(id) && !result.contains(id)) {
					result.add(id);
				}
				if (result.size() >= 3) {
					break;
				}
			}
		}
		if (result.isEmpty()) {
			return guideRepository.findAvailableOrderByRatingDesc(PageRequest.of(0, 3)).stream()
					.map(Guide::getId)
					.toList();
		}
		return result;
	}

	private static String cleanJson(String raw) {
		String trimmed = raw.trim();
		if (trimmed.startsWith("```")) {
			int start = trimmed.indexOf('{');
			int end = trimmed.lastIndexOf('}');
			if (start >= 0 && end > start) {
				return trimmed.substring(start, end + 1);
			}
		}
		return trimmed;
	}
}
