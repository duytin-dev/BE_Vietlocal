package com.vietlocal.app.config;

import com.vietlocal.app.service.ai.GeminiAiProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AiStartupLogger implements ApplicationRunner {

	private final AppProperties appProperties;
	private final GeminiAiProvider geminiAiProvider;

	@Override
	public void run(ApplicationArguments args) {
		String provider = appProperties.getAi().getProvider();
		if ("gemini".equalsIgnoreCase(provider) && geminiAiProvider.isConfigured()) {
			log.info("AI provider: Gemini (model={})", appProperties.getAi().getGemini().getModel());
		} else if ("gemini".equalsIgnoreCase(provider)) {
			log.warn("AI provider: gemini được chọn nhưng thiếu GEMINI_API_KEY → sẽ dùng stub demo");
		} else {
			log.info("AI provider: stub (demo)");
		}
	}
}
