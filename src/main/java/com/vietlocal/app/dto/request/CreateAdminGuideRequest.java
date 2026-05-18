package com.vietlocal.app.dto.request;

import com.vietlocal.app.domain.enums.GuideTier;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAdminGuideRequest {

	@NotBlank(message = "name is required")
	@Size(max = 255)
	private String name;

	/** Để trống → tự tạo từ tên (vd. nguyen-van-a). */
	@Size(max = 255)
	private String slug;

	@NotNull(message = "tier is required")
	private GuideTier tier;

	@Size(max = 5000)
	private String bio;

	@Size(max = 5000)
	private String styleDescription;

	@Size(max = 512)
	private String imageUrl;

	@Size(max = 255)
	private String languages;

	@Min(0)
	@Max(5)
	private Double rating;

	@NotNull(message = "pricePerDay is required")
	@DecimalMin(value = "0", inclusive = false, message = "pricePerDay must be positive")
	private BigDecimal pricePerDay;
}
