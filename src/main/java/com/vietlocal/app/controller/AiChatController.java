package com.vietlocal.app.controller;

import com.vietlocal.app.dto.request.AiChatRequest;
import com.vietlocal.app.dto.response.AiChatResponse;
import com.vietlocal.app.service.AiChatService;
import com.vietlocal.app.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiChatController {

	private final AiChatService aiChatService;

	@PostMapping("/chat")
	public ApiResponse<AiChatResponse> chat(@Valid @RequestBody AiChatRequest request) {
		return ApiResponse.ok(aiChatService.chat(request));
	}
}
