package com.vietlocal.app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookingRequest {

	@NotBlank(message = "customerName is required")
	private String customerName;

	@NotBlank
	@Email(message = "email must be valid")
	private String email;

	private String phone;

	@NotNull(message = "guideId is required")
	private Long guideId;

	/** Tên điểm đến (vd. Huế) — dùng tạo tên chuyến đi. */
	private String destinationName;

	/** Tên chuyến đi hiển thị (vd. Hà Nội Võ Thị Lan). Nếu trống, server tự ghép từ điểm đến + HDV. */
	@Size(max = 255)
	private String tripTitle;

	private String itinerarySummary;

	private String customerNotes;

	@NotNull(message = "estimatedDays is required")
	@Min(value = 1, message = "estimatedDays must be at least 1")
	@Max(value = 30, message = "estimatedDays must be at most 30")
	private Integer estimatedDays;
}
