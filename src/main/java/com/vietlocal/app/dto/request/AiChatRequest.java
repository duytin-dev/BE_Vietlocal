package com.vietlocal.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiChatRequest {

	private String sessionId;

	@NotBlank(message = "message is required")
	private String message;
}
