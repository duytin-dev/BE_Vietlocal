package com.vietlocal.app.dto.response;

import com.vietlocal.app.domain.enums.GuideTier;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GuideResponse {
	private Long id;
	private String name;
	private String slug;
	private GuideTier tier;
	private String bio;
	private String styleDescription;
	private String imageUrl;
	private String languages;
	private double rating;
	private BigDecimal pricePerDay;
	private boolean available;
}
