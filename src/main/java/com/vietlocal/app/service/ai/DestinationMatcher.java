package com.vietlocal.app.service.ai;

import com.vietlocal.app.domain.entity.Destination;
import com.vietlocal.app.repository.DestinationRepository;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DestinationMatcher {

	private static final Map<String, List<String>> EXTRA_ALIASES = Map.ofEntries(
			Map.entry("ha-long", List.of("vinh ha long", "halong bay")),
			Map.entry("ha-noi", List.of("thu do", "capital hanoi")),
			Map.entry("hoi-an", List.of("pho co hoi an")),
			Map.entry("da-nang", List.of("danang city")),
			Map.entry("sa-pa", List.of("tay bac", "fansipan")));

	private final DestinationRepository destinationRepository;

	public Optional<Destination> match(String message) {
		if (message == null || message.isBlank()) {
			return Optional.empty();
		}
		String norm = normalize(message);
		for (KeywordEntry entry : buildKeywords()) {
			if (norm.contains(entry.keyword())) {
				return Optional.of(entry.destination());
			}
		}
		return Optional.empty();
	}

	private List<KeywordEntry> buildKeywords() {
		List<KeywordEntry> list = new ArrayList<>();
		for (Destination d : destinationRepository.findAll()) {
			addKeyword(list, d, normalize(d.getName()));
			addKeyword(list, d, normalize(d.getSlug().replace('-', ' ')));
			List<String> extras = EXTRA_ALIASES.get(d.getSlug());
			if (extras != null) {
				for (String alias : extras) {
					addKeyword(list, d, normalize(alias));
				}
			}
		}
		list.sort(Comparator.comparingInt((KeywordEntry e) -> e.keyword().length()).reversed());
		return list;
	}

	private static void addKeyword(List<KeywordEntry> list, Destination d, String keyword) {
		if (keyword != null && keyword.length() >= 3) {
			list.add(new KeywordEntry(keyword, d));
		}
	}

	static String normalize(String text) {
		if (text == null) {
			return "";
		}
		String nfd = Normalizer.normalize(text, Normalizer.Form.NFD);
		String stripped = nfd.replaceAll("\\p{M}+", "");
		return stripped.toLowerCase(Locale.ROOT).replaceAll("\\s+", " ").trim();
	}

	private record KeywordEntry(String keyword, Destination destination) {
	}
}
