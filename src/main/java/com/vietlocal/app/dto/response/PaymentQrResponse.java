package com.vietlocal.app.dto.response;

import com.vietlocal.app.domain.enums.PaymentStatus;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentQrResponse {
	private Long bookingId;
	private Long paymentId;
	private BigDecimal amount;
	private String currency;
	private String qrCodeUrl;
	private String transactionRef;
	private PaymentStatus status;
}
