package com.vietlocal.app.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ai_chat_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiChatLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "session_id", nullable = false, length = 64)
	private String sessionId;

	@Column(name = "user_message", nullable = false, columnDefinition = "TEXT")
	private String userMessage;

	@Column(name = "ai_reply", columnDefinition = "TEXT")
	private String aiReply;

	@Column(name = "suggested_itinerary", columnDefinition = "TEXT")
	private String suggestedItinerary;

	@Column(name = "suggested_guide_ids", length = 128)
	private String suggestedGuideIds;

	@Builder.Default
	private Instant createdAt = Instant.now();
}
