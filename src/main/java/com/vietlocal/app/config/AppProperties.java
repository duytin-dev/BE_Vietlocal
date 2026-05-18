package com.vietlocal.app.config;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppProperties {

	private boolean seedEnabled = true;
	private Cors cors = new Cors();
	private Ai ai = new Ai();
	private Payment payment = new Payment();
	private Admin admin = new Admin();
	private Auth auth = new Auth();
	private Flyway flyway = new Flyway();

	@Getter
	@Setter
	public static class Flyway {
		/** Dev: repair failed migrations (e.g. V6) before migrate. Prod: set false. */
		private boolean repairOnStart = true;
	}

	@Getter
	@Setter
	public static class Auth {
		private Jwt jwt = new Jwt();
	}

	@Getter
	@Setter
	public static class Jwt {
		private String secret = "vietlocal-dev-jwt-secret-change-in-production-min-32";
		private long expirationMs = 604_800_000L; // 7 days
	}

	@Getter
	@Setter
	public static class Cors {
		private List<String> allowedOrigins = List.of("http://localhost:3000", "http://localhost:5173");
	}

	@Getter
	@Setter
	public static class Ai {
		/** stub | gemini */
		private String provider = "stub";
		private Gemini gemini = new Gemini();
	}

	@Getter
	@Setter
	public static class Gemini {
		private String apiKey = "";
		/** Model chính; nếu 429 sẽ thử fallbackModels */
		private String model = "gemini-2.5-flash-lite";
		private java.util.List<String> fallbackModels = java.util.List.of(
				"gemini-flash-lite-latest",
				"gemini-2.5-flash");
		private boolean fallbackToStub = true;
	}

	@Getter
	@Setter
	public static class Payment {
		private String webhookSecret = "vietlocal-dev-secret";
	}

	@Getter
	@Setter
	public static class Admin {
		private String secret = "vietlocal-admin-dev";
	}
}
