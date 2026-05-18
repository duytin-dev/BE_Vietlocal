package com.vietlocal.app.service.ai.gemini;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** DTOs for Google Generative Language API (Gemini). */
public final class GeminiApiModels {

	private GeminiApiModels() {
	}

	public record GenerateContentRequest(
			List<Content> contents,
			@JsonProperty("systemInstruction") Content systemInstruction,
			@JsonProperty("generationConfig") GenerationConfig generationConfig) {
	}

	public record Content(List<Part> parts) {
		public static Content ofText(String text) {
			return new Content(List.of(new Part(text)));
		}
	}

	public record Part(String text) {
	}

	public record GenerationConfig(
			double temperature,
			@JsonProperty("maxOutputTokens") int maxOutputTokens,
			@JsonProperty("responseMimeType") String responseMimeType) {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record GenerateContentResponse(List<Candidate> candidates) {
		public String firstText() {
			if (candidates == null || candidates.isEmpty()) {
				return null;
			}
			Candidate c = candidates.get(0);
			if (c.content() == null || c.content().parts() == null || c.content().parts().isEmpty()) {
				return null;
			}
			return c.content().parts().get(0).text();
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Candidate(Content content) {
	}

	/** Parsed JSON from model (our schema). */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record PlannerPayload(
			String reply,
			String suggestedItinerary,
			List<Long> suggestedGuideIds,
			Long detectedDestinationId) {
	}
}
