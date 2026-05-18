package com.vietlocal.app.service.ai;

import com.vietlocal.app.domain.entity.Destination;
import com.vietlocal.app.dto.response.AiChatResponse;
import com.vietlocal.app.repository.GuideRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StubAiProvider implements AiProvider {

	private final GuideRepository guideRepository;
	private final DestinationMatcher destinationMatcher;

	@Override
	public AiChatResponse chat(String sessionId, String message) {
		Optional<Destination> destination = destinationMatcher.match(message);
		String itinerary = destination
				.map(d -> StubItineraryTemplates.forSlug(d.getSlug()))
				.orElse(StubItineraryTemplates.forSlug(null));

		String reply = destination
				.map(d -> "Cảm ơn bạn! VietLocal đã chọn gợi ý chuyến đi " + d.getName()
						+ " cho bạn:\n\n" + itinerary
						+ "\n\nLịch trình bên dưới đã được chọn sẵn — bạn có thể đổi HDV hoặc bấm đặt tour ngay.")
				.orElse("Cảm ơn bạn đã chia sẻ! VietLocal gợi ý lịch trình:\n\n"
						+ itinerary
						+ "\n\nHãy nêu rõ điểm đến (ví dụ: Hà Nội, Hạ Long) để nhận gợi ý chính xác hơn.");

		List<Long> guideIds = guideRepository
				.findAvailableOrderByRatingDesc(PageRequest.of(0, 3))
				.stream()
				.map(g -> g.getId())
				.toList();

		AiChatResponse.AiChatResponseBuilder builder = AiChatResponse.builder()
				.sessionId(sessionId)
				.reply(reply)
				.suggestedItinerary(itinerary)
				.suggestedGuideIds(guideIds)
				.provider("stub");

		destination.ifPresent(d -> builder
				.detectedDestinationId(d.getId())
				.detectedDestinationName(d.getName())
				.detectedDestinationImageUrl(d.getImageUrl()));

		return builder.build();
	}
}
