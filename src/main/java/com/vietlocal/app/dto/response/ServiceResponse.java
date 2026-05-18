package com.vietlocal.app.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServiceResponse {
	private Long id;
	private String name;
	private String slug;
	private String description;
	private String iconUrl;
	private int sortOrder;
}
