package com.vietlocal.app;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class BookingIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void bookingFlow_createQrConfirm() throws Exception {
		String createBody = """
				{
				  "customerName": "Test User",
				  "email": "test@example.com",
				  "phone": "0900000000",
				  "guideId": 1,
				  "itinerarySummary": "Tour test",
				  "estimatedDays": 3
				}
				""";

		MvcResult createResult = mockMvc.perform(post("/api/bookings")
						.contentType(MediaType.APPLICATION_JSON)
						.content(createBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.totalAmount").value(4500000))
				.andReturn();

		String response = createResult.getResponse().getContentAsString();
		long bookingId = extractId(response);

		mockMvc.perform(get("/api/bookings/" + bookingId + "/payment-qr"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.status").value("PENDING"));

		mockMvc.perform(post("/api/bookings/" + bookingId + "/confirm-payment")
						.header("X-Payment-Secret", "test-secret"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.status").value("PAID"));

		mockMvc.perform(post("/api/bookings/" + bookingId + "/confirm-payment")
						.header("X-Payment-Secret", "test-secret"))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.errorCode").value("PAYMENT_ALREADY_PAID"));
	}

	@Test
	void confirmPayment_withoutSecret_returnsUnauthorized() throws Exception {
		mockMvc.perform(post("/api/bookings/1/confirm-payment"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.errorCode").value("UNAUTHORIZED"));
	}

	private long extractId(String json) {
		int idx = json.indexOf("\"id\":");
		int start = idx + 5;
		int end = json.indexOf(',', start);
		if (end < 0) {
			end = json.indexOf('}', start);
		}
		return Long.parseLong(json.substring(start, end).trim());
	}
}
