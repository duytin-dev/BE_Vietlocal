package com.vietlocal.app.service;

import com.vietlocal.app.domain.entity.Guide;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class PricingService {

	public BigDecimal calculateTotal(Guide guide, int estimatedDays) {
		return guide.getPricePerDay().multiply(BigDecimal.valueOf(estimatedDays));
	}
}
