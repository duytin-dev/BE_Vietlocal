package com.vietlocal.app.service.ai;

import com.vietlocal.app.dto.response.AiChatResponse;

public interface AiProvider {

	AiChatResponse chat(String sessionId, String message);
}
