package com.vietlocal.app.utils;

public final class TripTitleHelper {

	private TripTitleHelper() {
	}

	public static String build(String tripOrDestinationName, String guideName) {
		if (tripOrDestinationName != null && !tripOrDestinationName.isBlank()) {
			return tripOrDestinationName.trim();
		}
		if (guideName != null && !guideName.isBlank()) {
			return guideName.trim();
		}
		return "Tour";
	}
}
