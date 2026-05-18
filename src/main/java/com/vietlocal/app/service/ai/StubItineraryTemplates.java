package com.vietlocal.app.service.ai;

import java.util.Map;

final class StubItineraryTemplates {

	private static final Map<String, String> BY_SLUG = Map.of(
			"da-nang",
			"Ngày 1: Bán đảo Sơn Trà & biển Mỹ Khê\nNgày 2: Bà Nà Hills & Cầu Vàng\nNgày 3: Ẩm thực & chợ đêm",
			"ha-noi",
			"Ngày 1: Phố cổ & Hồ Hoàn Kiếm\nNgày 2: Văn Miếu & Lăng Bác\nNgày 3: Ẩm thực đường phố & làng gốm",
			"sa-pa",
			"Ngày 1: Fansipan & Cát Cát\nNgày 2: Ruộng bậc thang\nNgày 3: Chợ phiên & homestay bản địa",
			"hoi-an",
			"Ngày 1: Phố cổ & Chùa Cầu\nNgày 2: Làng rau Trà Quế & làng gốm\nNgày 3: Biển An Bàng & đèn lồng",
			"hue",
			"Ngày 1: Đại Nội & sông Hương\nNgày 2: Lăng tẩm & ẩm thực cung đình\nNgày 3: Làng nghề & chợ Đông Ba",
			"nha-trang",
			"Ngày 1: Bãi biển & Tháp Bà\nNgày 2: Tour đảo & lặn san hô\nNgày 3: VinWonders & hải sản",
			"phu-quoc",
			"Ngày 1: Bãi Sao & Grand World\nNgày 2: Làng chài & nhà thùng nước mắm\nNgày 3: Cáp treo Hòn Thơm",
			"da-lat",
			"Ngày 1: Hồ Xuân Hương & đồi chè\nNgày 2: Thác Datanla & làng hoa\nNgày 3: Cafe & săn mây",
			"can-tho",
			"Ngày 1: Chợ nổi Cái Răng\nNgày 2: Miệt vườn & đờn ca tài tử\nNgày 3: Bến Ninh Kiều & ẩm thực miền Tây",
			"ha-long",
			"Ngày 1: Du thuyền & hang Sửng Sốt\nNgày 2: Kayak & bãi Titop\nNgày 3: Làng chài & hải sản vịnh");

	private static final String GENERIC =
			"Ngày 1: Điểm đến nổi bật\nNgày 2: Ẩm thực địa phương\nNgày 3: Văn hóa bản địa";

	private StubItineraryTemplates() {
	}

	static String forSlug(String slug) {
		if (slug == null) {
			return GENERIC;
		}
		return BY_SLUG.getOrDefault(slug, GENERIC);
	}
}
