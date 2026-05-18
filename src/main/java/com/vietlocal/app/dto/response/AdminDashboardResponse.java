package com.vietlocal.app.dto.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminDashboardResponse {
	private long totalBookings;
	private long pendingPaymentBookings;
	private long paidBookings;
	private BigDecimal totalRevenue;
	private long totalUsers;
	private long totalAiChats;
	private long totalGuides;
}
