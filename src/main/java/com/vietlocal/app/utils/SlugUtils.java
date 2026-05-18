package com.vietlocal.app.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.function.Predicate;

public final class SlugUtils {

	private SlugUtils() {
	}

	public static String toSlug(String input) {
		if (input == null || input.isBlank()) {
			return "item";
		}
		String normalized = Normalizer.normalize(input.trim(), Normalizer.Form.NFD)
				.replaceAll("\\p{M}", "");
		String slug = normalized
				.toLowerCase(Locale.ROOT)
				.replaceAll("[^a-z0-9\\s-]", "")
				.replaceAll("[\\s_]+", "-")
				.replaceAll("-+", "-")
				.replaceAll("^-|-$", "");
		return slug.isBlank() ? "item" : slug;
	}

	public static String uniqueSlug(String base, Predicate<String> exists) {
		String slug = toSlug(base);
		if (!exists.test(slug)) {
			return slug;
		}
		for (int i = 2; i < 1000; i++) {
			String candidate = slug + "-" + i;
			if (!exists.test(candidate)) {
				return candidate;
			}
		}
		return slug + "-" + System.currentTimeMillis();
	}
}
