package com.vietlocal.app;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AiChatIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void chat_returnsItineraryAndGuideIds() throws Exception {
		mockMvc.perform(post("/api/ai/chat")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"message\":\"Tôi muốn đi Đà Nẵng 3 ngày\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.reply").isNotEmpty())
				.andExpect(jsonPath("$.data.suggestedItinerary").isNotEmpty())
				.andExpect(jsonPath("$.data.suggestedGuideIds").isArray())
				.andExpect(jsonPath("$.data.detectedDestinationName").value("Đà Nẵng"));
	}

	@Test
	void chat_detectsHaLongFromMessage() throws Exception {
		mockMvc.perform(post("/api/ai/chat")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"message\":\"Đi vịnh Hạ Long đi\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.detectedDestinationName").value("Hạ Long"))
				.andExpect(jsonPath("$.data.suggestedItinerary").isNotEmpty());
	}
}
