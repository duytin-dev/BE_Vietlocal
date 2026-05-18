package com.vietlocal.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.vietlocal.app.utils.TripTitleHelper;
import org.junit.jupiter.api.Test;

class TripTitleHelperTest {

	@Test
	void build_usesTripNameOnly() {
		assertEquals("Ẩm thực đường phố Hà Nội", TripTitleHelper.build("Ẩm thực đường phố Hà Nội", "Võ Thị Lan"));
	}

	@Test
	void build_fallsBackToGuideWhenNoTripName() {
		assertEquals("Võ Thị Lan", TripTitleHelper.build(null, "Võ Thị Lan"));
	}
}
