package com.vietlocal.app.dto.response;

import com.vietlocal.app.domain.enums.UserRole;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminUserResponse {
	private Long id;
	private String email;
	private String fullName;
	private UserRole role;
	private Instant createdAt;
	private long bookingCount;
}
