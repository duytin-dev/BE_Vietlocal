package com.vietlocal.app.config;

import com.vietlocal.app.domain.entity.BlogPost;
import com.vietlocal.app.domain.entity.Booking;
import com.vietlocal.app.domain.entity.BrandStory;
import com.vietlocal.app.domain.entity.Destination;
import com.vietlocal.app.domain.entity.DestinationTrip;
import com.vietlocal.app.domain.entity.Guide;
import com.vietlocal.app.domain.entity.Payment;
import com.vietlocal.app.domain.entity.ServiceOffering;
import com.vietlocal.app.domain.enums.BookingStatus;
import com.vietlocal.app.domain.enums.GuideTier;
import com.vietlocal.app.domain.enums.PaymentStatus;
import com.vietlocal.app.repository.BlogPostRepository;
import com.vietlocal.app.repository.BookingRepository;
import com.vietlocal.app.repository.BrandStoryRepository;
import com.vietlocal.app.repository.DestinationRepository;
import com.vietlocal.app.repository.DestinationTripRepository;
import com.vietlocal.app.repository.GuideRepository;
import com.vietlocal.app.repository.PaymentRepository;
import com.vietlocal.app.repository.ServiceOfferingRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.seed", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DataInitializer implements CommandLineRunner {

	private final BrandStoryRepository brandStoryRepository;
	private final DestinationRepository destinationRepository;
	private final DestinationTripRepository destinationTripRepository;
	private final BlogPostRepository blogPostRepository;
	private final ServiceOfferingRepository serviceOfferingRepository;
	private final GuideRepository guideRepository;
	private final BookingRepository bookingRepository;
	private final PaymentRepository paymentRepository;

	@Override
	public void run(String... args) {
		if (brandStoryRepository.count() > 0) {
			seedDestinationTripsIfEmpty();
			return;
		}

		seedBrandStories();
		List<Destination> destinations = seedDestinations();
		seedDestinationTrips(destinations);
		seedBlogPosts(destinations);
		seedServices();
		List<Guide> guides = seedGuides();
		seedBookingsAndPayments(guides);
	}

	private void seedBrandStories() {
		String[][] data = {
				{"VietLocal — Du lịch địa phương", "Cùng người bản địa, khám phá Việt Nam thật",
						"VietLocal ra đời từ niềm tin du lịch bền vững và tôn trọng văn hóa địa phương."},
				{"Sứ mệnh của chúng tôi", "Kết nối, chia sẻ, bảo tồn",
						"Mang đến trải nghiệm chân thực, hỗ trợ cộng đồng và hướng dẫn viên bản địa."},
				{"Tầm nhìn 2030", "Việt Nam hiện diện trên bản đồ du lịch có trách nhiệm",
						"Trở thành nền tảng du lịch địa phương hàng đầu Đông Nam Á."},
				{"Giá trị cốt lõi", "Chân thật — Tôn trọng — Bền vững",
						"Mỗi tour đều được thiết kế với sự am hiểu địa phương và trách nhiệm xã hội."},
				{"Cộng đồng hướng dẫn viên", "Hơn 500 đối tác trên khắp Việt Nam",
						"Chúng tôi đào tạo và đồng hành cùng HDV để nâng cao chất lượng dịch vụ."},
				{"Trải nghiệm ẩm thực", "Từ chợ quê đến michelin địa phương",
						"Food tour do người bản địa dẫn — không chỉ ăn mà còn hiểu câu chuyện món ăn."},
				{"Du lịch có trách nhiệm", "Ít tác động, nhiều ý nghĩa",
						"Ưu tiên làng nghề, homestay và hoạt động thân thiện môi trường."},
				{"Câu chuyện khách hàng", "Hành trình của hàng nghìn du khách",
						"Phản hồi tích cực từ gia đình, cặp đôi và nhóm bạn trẻ khắp thế giới."},
				{"Đối tác & liên kết", "Cùng phát triển du lịch Việt",
						"Hợp tác với địa phương, HTX và doanh nghiệp vừa và nhỏ tại từng vùng miền."},
				{"Liên hệ VietLocal", "Chúng tôi luôn lắng nghe bạn",
						"Đội ngũ tư vấn 24/7 hỗ trợ lên lịch trình và chọn hướng dẫn viên phù hợp."}
		};
		for (int i = 0; i < data.length; i++) {
			brandStoryRepository.save(BrandStory.builder()
					.title(data[i][0])
					.tagline(data[i][1])
					.content(data[i][2])
					.heroImageUrl("https://images.unsplash.com/photo-1559592413-7cec4d0cae2b?w=1200&sig=" + i)
					.build());
		}
	}

	private static final String IMG_Q = "?auto=format&fit=crop&w=1000&q=80";

	private static final java.util.Map<String, String> DESTINATION_IMAGE_URLS = java.util.Map.of(
			"da-nang", "https://images.unsplash.com/photo-1569154941061-e231b4725ef1" + IMG_Q,
			"ha-noi", "https://images.unsplash.com/photo-1605538032432-a9f0c8d9baac" + IMG_Q,
			"sa-pa", "https://images.unsplash.com/photo-1506905925346-21bda4d32df4" + IMG_Q,
			"hoi-an", "https://images.unsplash.com/photo-1528127269322-539801943592" + IMG_Q,
			"hue", "https://images.unsplash.com/photo-1524231757912-21f4fe3a7200" + IMG_Q,
			"nha-trang", "https://images.unsplash.com/photo-1583212292454-1fe6229603b7" + IMG_Q,
			"phu-quoc", "https://images.unsplash.com/photo-1514282401047-d79a71a590e8" + IMG_Q,
			"da-lat", "https://images.unsplash.com/photo-1472214103451-9374bd1c798e" + IMG_Q,
			"can-tho", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e" + IMG_Q,
			"ha-long", "https://images.unsplash.com/photo-1524231757912-21f4fe3a7200" + IMG_Q);

	private List<Destination> seedDestinations() {
		// name, slug, region, summary (≤500), description, featured
		String[][] data = {
				{"Đà Nẵng", "da-nang", "Miền Trung",
						"Thành phố biển năng động: cầu Rồng, Bà Nà, Mỹ Khê và ẩm thực đường phố đậm chất miền Trung.",
						"""
						Đà Nẵng là điểm đến lý tưởng cho du lịch kết hợp biển, núi và văn hóa. Buổi tối nên xem \
						cầu Rồng phun lửa (cuối tuần), tham quan Bà Nà Hills với Cầu Vàng, tắm biển Mỹ Khê hoặc \
						Non Nước. Ẩm thực nổi bật: mì Quảng, bánh tráng cuốn thịt heo, bún chả cá, hải sản chợ \
						đêm Helio. Thời điểm đẹp: tháng 3–8 (ít mưa). Gợi ý lưu trú quanh biển Sơn Trà hoặc trung \
						tâm Hải Châu để di chuyển thuận tiện. VietLocal có hướng dẫn viên địa phương hỗ trợ tour \
						ngắn ngày, food tour và trải nghiệm làng nghề.""",
						"true"},
				{"Hà Nội", "ha-noi", "Miền Bắc",
						"Thủ đô nghìn năm văn hiến: Hồ Gươm, phố cổ, ẩm thực đường phố và không gian cà phê vintage.",
						"""
						Hà Nội mang vẻ đẹp cổ kính xen lẫn nhịp sống hiện đại. Đi bộ quanh Hồ Hoàn Kiếm, Văn Miếu, \
						Lăng Bác, Phố cổ 36 phố phường; buổi tối thưởng thức bia hơi Ta Hien. Món nên thử: phở \
						bò, bún chả, chả cá Lã Vọng, bánh cuốn Thanh Trì, cốm Vòng. Mùa thu (tháng 9–11) và \
						xuân (tháng 2–4) là thời điểm dễ chịu nhất. Có thể kết hợp chuyến đi ngắn đến Ninh Bình \
						hoặc Hạ Long từ Hà Nội. VietLocal kết nối bạn với guide am hiểu lịch sử, ẩm thực và \
						trải nghiệm văn hóa địa phương.""",
						"true"},
				{"Sa Pa", "sa-pa", "Miền Bắc",
						"Vùng núi cao Tây Bắc: ruộng bậc thang, Fansipan, bản làng dân tộc và khí hậu mát quanh năm.",
						"""
						Sa Pa thu hút du khách bởi cảnh quan núi non hùng vĩ và đa dạng văn hóa các dân tộc \
						(H'Mông, Dao, Tày…). Trekking qua ruộng bậc thang Mù Cang Chải (mùa nước đổ tháng \
						5–6), chinh phục Fansipan bằng cáp treo hoặc leo núi, homestay tại Lao Chải, Ta Van. \
						Chợ phiên cuối tuần là dịp trải nghiệm đời sống bản địa. Nên mang áo ấm vì sáng sớm \
						lạnh; mùa lúa chín tháng 9–10 rất đẹp. Ẩm thực: thịt trâu gác bếp, cá hồi, rượu ngô. \
						VietLocal có guide bản địa dẫn trek an toàn và chia sẻ câu chuyện văn hóa.""",
						"true"},
				{"Hội An", "hoi-an", "Miền Trung",
						"Phố cổ UNESCO: đèn lồng, hội An sông Thu Bồn, làng gốm Thanh Hà và ẩm thực cao lầu.",
						"""
						Hội An là điểm đến lãng mạn và văn hóa, đặc biệt về đêm khi phố cổ thắp sáng đèn lồng. \
						Tham quan Chùa Cầu, nhà cổ, làng rau Trà Quế, làng gốm Thanh Hà, thả đèn hoa đăng \
						trên sông Hoài. Gần đó có biển An Bàng, Cửa Đại. Món đặc sản: cao lầu, mì Quảng, \
						bánh mì Phượng, bánh bao bánh vạc. Nên đi tháng 2–4 hoặc tháng 8–10; tránh mùa mưa \
						bão. Có dịch vụ may đo nhanh trong 24h. VietLocal hỗ trợ tour phố cổ, nấu ăn và \
						trải nghiệm làng nghề truyền thống.""",
						"true"},
				{"Huế", "hue", "Miền Trung",
						"Cố đô triều Nguyễn: Đại Nội, lăng tẩm, sông Hương và ẩm thực cung đình tinh tế.",
						"""
						Huế mang dấu ấn lịch sử sâu sắc với quần thể Đại Nội, lăng Khải Định, lăng Tự Đức, \
						chùa Thiên Mụ. Du thuyền sông Hương, nghe ca Huế trên thuyền rồng. Ẩm thực nổi tiếng: \
						bún bò Huế, cơm hến, bánh bèo, bánh khoái, chè Huế. Thành phố trầm lắng, phù hợp \
						du lịch chậm 2–3 ngày; kết hợp Lăng Cô, đèo Hải Vân khi di chuyển Đà Nẵng–Huế. \
						Mùa đẹp: tháng 1–4. VietLocal có guide am hiểu lịch sử triều Nguyễn và ẩm thực \
						địa phương.""",
						"false"},
				{"Nha Trang", "nha-trang", "Miền Trung",
						"Thành phố biển nhiệt đới: VinWonders, lặn san hô, đảo và hải sản tươi sống.",
						"""
						Nha Trang nổi bật với bãi biển dài, nắng đẹp quanh năm và nhiều hoạt động biển. \
						Tham quan VinWonders, Tháp Bà Ponagar, Viện Hải dương học; tour đảo Hòn Mun, \
						Hòn Tằm để lặn ngắm san hô. Ẩm thực: nem nướng Ninh Hòa, bún cá, hải sản chợ \
						Đầm. Thời điểm tốt: tháng 2–8; tránh mùa mưa cuối năm. Phù hợp gia đình và \
						du lịch nghỉ dưỡng. VietLocal hỗ trợ đặt tour đảo, snorkeling và trải nghiệm \
						ẩm thực địa phương.""",
						"false"},
				{"Phú Quốc", "phu-quoc", "Miền Nam",
						"Đảo ngọc Kiên Giang: bãi Sao, Grand World, nước mắm, hải sản và hoàng hôn trên biển.",
						"""
						Phú Quốc là thiên đường nghỉ dưỡng với bãi biển trong xanh, rừng nguyên sinh và \
						resort cao cấp. Nên ghé bãi Sao, bãi Dài, Grand World, chợ đêm Dinh Cậu, nhà thùng \
						nước mắm, trại ngọc trai. Hoạt động: lặn, câu mực đêm, cáp treo Hòn Thơm. Đặc sản: \
						gỏi cá trích, nhím biển nướng, hải sản. Mùa khô tháng 11–4 thuận tiện nhất. \
						VietLocal có guide địa phương tour bãi biển, chợ và trải nghiệm làng chài.""",
						"true"},
				{"Đà Lạt", "da-lat", "Miền Nam",
						"Thành phố ngàn hoa trên cao nguyên: thác Datanla, đồi chè, cafe và không khí se lạnh.",
						"""
						Đà Lạt mang khí hậu mát mẻ quanh năm, phù hợp nghỉ dưỡng và chụp ảnh. Điểm tham quan: \
						Hồ Xuân Hương, Thung lũng Tình yêu, Dinh Bảo Đại, thác Datanla, Prenn, đồi chè Cầu \
						Đất, làng hoa. Ẩm thực: bánh căn, bánh tráng nướng, lẩu gà lá é, dâu tây, atiso. \
						Mùa hoa dã quỳ tháng 10–11, mùa cherry blossom tháng 1–2. Nên thuê xe máy để \
						khám phá. VietLocal hỗ trợ tour săn mây, cafe và homestay vườn hoa.""",
						"false"},
				{"Cần Thơ", "can-tho", "Miền Nam",
						"Trung tâm Đồng bằng sông Cửu Long: chợ nổi Cái Răng, miệt vườn và văn hóa sông nước.",
						"""
						Cần Thơ là cửa ngõ khám phá miền Tây với chợ nổi Cái Răng (nên đi sáng sớm 5–7h), \
						chùa Ông, bến Ninh Kiều, khu du lịch Mỹ Khánh, vườn trái cây. Trải nghiệm: chèo \
						sampan, nghe đờn ca tài tử, thưởng thức lẩu mắm, bánh pía, chuột quay, bún riêu \
						cua. Thích hợp tour 1–2 ngày từ TP.HCM. Mùa nước nổi tháng 8–11 có cảnh đẹp đặc \
						trưng. VietLocal có guide miền Tây dẫn chợ nổi và tour miệt vườn.""",
						"false"},
				{"Hạ Long", "ha-long", "Miền Bắc",
						"Vịnh di sản thế giới: đảo đá vôi, du thuyền qua đêm và kayak giữa hang động kỳ vĩ.",
						"""
						Vịnh Hạ Long thuộc top điểm đến biểu tượng của Việt Nam với hàng nghìn đảo đá vôi \
						nhấp nhô trên mặt nước ngọc bích. Tour 1–2 ngày: du thuyền sang trọng hoặc \
						budget, kayak hang Luồn, Sửng Sốt, tắm biển Titop, làng chài Cửa Vạn. Kết hợp \
						Yên Tử, Cát Bà. Ẩm thực trên tàu: hải sản tươi, chả mực Hạ Long. Mùa đẹp: \
						tháng 10–4; tránh bão tháng 7–9. VietLocal hỗ trợ tư vấn tour và guide địa phương \
						tại cảng Tuần Châu, Hòn Gai.""",
						"true"}
		};
		List<Destination> list = new ArrayList<>();
		for (String[] row : data) {
			String slug = row[1];
			String imageUrl = DESTINATION_IMAGE_URLS.getOrDefault(slug,
					"https://images.unsplash.com/photo-1501785888041-af3ef245b9d2?auto=format&fit=crop&w=1000&q=80");
			list.add(destinationRepository.save(Destination.builder()
					.name(row[0])
					.slug(slug)
					.region(row[2])
					.summary(row[3].trim())
					.description(row[4].trim().replaceAll(" +", " "))
					.imageUrl(imageUrl)
					.featured(Boolean.parseBoolean(row[5]))
					.build()));
		}
		return list;
	}

	private void seedDestinationTripsIfEmpty() {
		if (destinationTripRepository.count() > 0) {
			return;
		}
		seedDestinationTrips(destinationRepository.findAll());
	}

	private void seedDestinationTrips(List<Destination> destinations) {
		if (destinations.isEmpty()) {
			return;
		}
		Map<String, Destination> bySlug = destinations.stream()
				.collect(Collectors.toMap(Destination::getSlug, Function.identity()));
		String[][] data = {
				{"da-nang", "Tour Bà Nà Hills & Cầu Vàng", "da-nang-ba-na", "Cầu Vàng, cáp treo và view núi", "1", "1"},
				{"da-nang", "Biển Mỹ Khê & Sơn Trà", "da-nang-bien", "Tắm biển và ngắm hoàng hôn", "1", "2"},
				{"da-nang", "Ẩm thực & phố đêm Đà Nẵng", "da-nang-am-thuc", "Mì Quảng, bánh tráng, chợ Helio", "2", "3"},
				{"ha-noi", "Phố cổ & Hồ Hoàn Kiếm", "ha-noi-pho-co", "Đi bộ 36 phố phường", "1", "1"},
				{"ha-noi", "Ẩm thực đường phố Hà Nội", "ha-noi-am-thuc", "Phở, bún chả, bia hơi", "1", "2"},
				{"ha-noi", "Làng gốm Bát Tràng", "ha-noi-bat-trang", "Trải nghiệm làm gốm", "1", "3"},
				{"sa-pa", "Trekking Fansipan", "sa-pa-fansipan", "Cáp treo hoặc leo núi", "2", "1"},
				{"sa-pa", "Homestay bản H'Mông", "sa-pa-homestay", "Sống cùng người bản địa", "2", "2"},
				{"sa-pa", "Ruộng bậc thang & chợ phiên", "sa-pa-ruong", "Cảnh đẹp mùa lúa", "2", "3"},
				{"hoi-an", "Phố cổ Hội An về đêm", "hoi-an-pho-co", "Đèn lồng và Chùa Cầu", "1", "1"},
				{"hoi-an", "Làng gốm Thanh Hà", "hoi-an-gom", "Làng nghề truyền thống", "1", "2"},
				{"hoi-an", "Ẩm thực cao lầu & biển An Bàng", "hoi-an-am-thuc", "Đặc sản và tắm biển", "2", "3"},
				{"hue", "Cố đô Huế một ngày", "hue-co-do", "Đại Nội và lăng tẩm", "1", "1"},
				{"hue", "Du thuyền sông Hương", "hue-song-huong", "Ca Huế trên thuyền rồng", "1", "2"},
				{"hue", "Ẩm thực cung đình Huế", "hue-am-thuc", "Bún bò, cơm hến, chè", "2", "3"},
				{"nha-trang", "Tour đảo & lặn san hô", "nha-trang-dao", "Hòn Mun, snorkeling", "1", "1"},
				{"nha-trang", "VinWonders & Tháp Bà", "nha-trang-vin", "Giải trí và văn hóa Chăm", "1", "2"},
				{"nha-trang", "Hải sản & biển Nha Trang", "nha-trang-bien", "Bãi biển và ẩm thực", "2", "3"},
				{"phu-quoc", "Bãi Sao & Grand World", "phu-quoc-bai-sao", "Biển và vui chơi", "2", "1"},
				{"phu-quoc", "Làng chài & nước mắm", "phu-quoc-lang-chai", "Trải nghiệm địa phương", "1", "2"},
				{"phu-quoc", "Cáp treo Hòn Thơm", "phu-quoc-cap-treo", "Ngắm toàn cảnh đảo", "1", "3"},
				{"da-lat", "Hồ Xuân Hương & đồi chè", "da-lat-ho-xuan-huong", "Khí hậu mát", "1", "1"},
				{"da-lat", "Săn mây & thác Datanla", "da-lat-san-may", "Thiên nhiên và adventure", "2", "2"},
				{"da-lat", "Cafe & làng hoa", "da-lat-cafe", "Chụp ảnh và thư giãn", "1", "3"},
				{"can-tho", "Chợ nổi Cái Răng", "can-tho-cho-noi", "Sáng sớm trên sông", "1", "1"},
				{"can-tho", "Miệt vườn miền Tây", "can-tho-miet-vuon", "Trái cây và đờn ca", "1", "2"},
				{"can-tho", "Bến Ninh Kiều & ẩm thực", "can-tho-ben-ninh-kieu", "Sông nước và lẩu mắm", "1", "3"},
				{"ha-long", "Du thuyền 2 ngày 1 đêm", "ha-long-du-thuyen", "Hang Sửng Sốt, tắm biển", "2", "1"},
				{"ha-long", "Kayak hang động", "ha-long-kayak", "Khám phá vịnh", "1", "2"},
				{"ha-long", "Bãi Titop & làng chài", "ha-long-titop", "View toàn vịnh", "1", "3"}
		};
		for (String[] row : data) {
			Destination dest = bySlug.get(row[0]);
			if (dest == null) {
				continue;
			}
			destinationTripRepository.save(DestinationTrip.builder()
					.destination(dest)
					.title(row[1])
					.slug(row[2])
					.summary(row[3])
					.durationDays(Integer.parseInt(row[4]))
					.sortOrder(Integer.parseInt(row[5]))
					.build());
		}
	}

	private void seedBlogPosts(List<Destination> destinations) {
		String[][] data = {
				{"5 món ăn phải thử ở Đà Nẵng", "5-mon-an-da-nang", "Mì Quảng, bánh tráng và hải sản tươi."},
				{"Phố cổ Hội An về đêm", "hoi-an-ve-dem", "Đèn lồng, múa rối nước và cao lầu."},
				{"Trekking Fansipan cho người mới", "trekking-fansipan", "Lộ trình an toàn và view đỉnh núi."},
				{"Ẩm thực đường phố Hà Nội", "am-thuc-ha-noi", "Bún chả, phở và bánh cuốn Thanh Trì."},
				{"Homestay Tây Bắc trải nghiệm", "homestay-tay-bac", "Sống cùng người H'Mông, thổ cẩm."},
				{"Biển Phú Quốc — gợi ý 3 ngày", "phu-quoc-3-ngay", "Bãi Sao, Grand World và hải sản."},
				{"Cố đô Huế một ngày", "hue-mot-ngay", "Đại Nội, chùa Thiên Mụ và chè Huế."},
				{"Đà Lạt — cafe và thác", "da-lat-cafe-thac", "Dinh Bảo Đại, Datanla và làng hoa."},
				{"Chợ nổi Cái Răng", "cho-noi-cai-rang", "Sáng sớm trên sông Hậu."},
				{"Du thuyền Hạ Long", "du-thuyen-ha-long", "Hang Sửng Sốt và kayak vịnh."}
		};
		for (int i = 0; i < data.length; i++) {
			blogPostRepository.save(BlogPost.builder()
					.title(data[i][0])
					.slug(data[i][1])
					.excerpt(data[i][2])
					.content("Bài viết chi tiết về " + data[i][0] + ". VietLocal gợi ý lịch trình và HDV địa phương.")
					.coverImageUrl("https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=800&sig=" + i)
					.destination(destinations.get(i % destinations.size()))
					.publishedAt(Instant.now().minus(i, ChronoUnit.DAYS))
					.build());
		}
	}

	private void seedServices() {
		String[][] data = {
				{"Hướng dẫn viên theo ngày", "huong-dan-vien-theo-ngay", "Đồng hành khám phá theo lịch cá nhân hóa."},
				{"Trải nghiệm ẩm thực", "trai-nghiem-am-thuc", "Food tour chợ và quán địa phương."},
				{"Tour văn hóa & làng nghề", "tour-van-hoa-lang-nghe", "Gặp nghệ nhân và học nghề thủ công."},
				{"Trekking thiên nhiên", "trekking-thien-nhien", "Trek nhẹ đến chuyên sâu."},
				{"Homestay bản địa", "homestay-ban-dia", "Ở cùng gia đình địa phương."},
				{"Tour chụp ảnh", "tour-chup-anh", "Góc chụp đẹp và storytelling."},
				{"Xe đạp khám phá", "xe-dap-kham-pha", "Đạp phố cổ và làng quê."},
				{"Tour đêm", "tour-dem", "Chợ đêm, ánh đèn và ẩm thực."},
				{"Workshop nấu ăn", "workshop-nau-an", "Học nấu món địa phương."},
				{"Tour gia đình trẻ em", "tour-gia-dinh", "Hoạt động an toàn cho trẻ."}
		};
		for (int i = 0; i < data.length; i++) {
			serviceOfferingRepository.save(ServiceOffering.builder()
					.name(data[i][0])
					.slug(data[i][1])
					.description(data[i][2])
					.iconUrl("https://api.iconify.design/mdi/map-marker.svg?sig=" + i)
					.sortOrder(i + 1)
					.build());
		}
	}

	private List<Guide> seedGuides() {
		String[][] data = {
				{"Nguyễn Minh An", "nguyen-minh-an", "PREMIUM", "1500000", "4.9", "Tiếng Việt, English"},
				{"Trần Thị Hương", "tran-thi-huong", "MID", "800000", "4.7", "Tiếng Việt, English"},
				{"Lê Văn Đức", "le-van-duc", "BUDGET", "400000", "4.5", "Tiếng Việt"},
				{"Phạm Thu Hà", "pham-thu-ha", "PREMIUM", "1600000", "4.8", "Tiếng Việt, Français"},
				{"Hoàng Quốc Bảo", "hoang-quoc-bao", "MID", "750000", "4.6", "Tiếng Việt, 中文"},
				{"Võ Thị Lan", "vo-thi-lan", "BUDGET", "450000", "4.4", "Tiếng Việt"},
				{"Đặng Minh Khoa", "dang-minh-khoa", "PREMIUM", "1700000", "5.0", "Tiếng Việt, English, 日本語"},
				{"Bùi Thanh Tâm", "bui-thanh-tam", "MID", "850000", "4.7", "Tiếng Việt"},
				{"Ngô Văn Hùng", "ngo-van-hung", "BUDGET", "380000", "4.3", "Tiếng Việt"},
				{"Lý Phương Anh", "ly-phuong-anh", "MID", "900000", "4.8", "Tiếng Việt, English"}
		};
		List<Guide> list = new ArrayList<>();
		for (String[] row : data) {
			list.add(guideRepository.save(Guide.builder()
					.name(row[0])
					.slug(row[1])
					.tier(GuideTier.valueOf(row[2]))
					.bio("Hướng dẫn viên " + row[0] + " — chuyên tour văn hóa và trải nghiệm địa phương.")
					.styleDescription("Phong cách " + row[2].toLowerCase() + ", nhiệt tình và am hiểu vùng miền.")
					.imageUrl("https://images.unsplash.com/photo-1529156069898-49953e39b3ac?w=400&sig=" + row[1])
					.languages(row[5])
					.rating(Double.parseDouble(row[4]))
					.pricePerDay(new BigDecimal(row[3]))
					.build()));
		}
		return list;
	}

	private void seedBookingsAndPayments(List<Guide> guides) {
		String[] names = {"Nguyễn Văn A", "Trần Thị B", "Lê Văn C", "Phạm Thị D", "Hoàng Văn E",
				"John Smith", "Emma Wilson", "Park Min-jun", "Marie Dubois", "Chen Wei"};
		BookingStatus[] statuses = {
				BookingStatus.PENDING, BookingStatus.CONFIRMED, BookingStatus.COMPLETED,
				BookingStatus.PENDING, BookingStatus.CONFIRMED,
				BookingStatus.COMPLETED, BookingStatus.PENDING, BookingStatus.CONFIRMED,
				BookingStatus.CANCELLED, BookingStatus.COMPLETED
		};

		for (int i = 0; i < 10; i++) {
			Guide guide = guides.get(i % guides.size());
			int days = (i % 5) + 1;
			BigDecimal total = guide.getPricePerDay().multiply(BigDecimal.valueOf(days));
			BookingStatus status = statuses[i];

			Booking booking = bookingRepository.save(Booking.builder()
					.customerName(names[i])
					.email("khach" + (i + 1) + "@example.com")
					.phone("090" + String.format("%07d", 1000000 + i))
					.guide(guide)
					.itinerarySummary("Lịch trình " + days + " ngày với HDV " + guide.getName())
					.customerNotes("Ghi chú mẫu cho booking #" + (i + 1))
					.status(status)
					.estimatedDays(days)
					.totalAmount(total)
					.createdAt(Instant.now().minus(i + 1, ChronoUnit.DAYS))
					.build());

			String ref = "VL-SEED" + String.format("%03d", i + 1);
			PaymentStatus paymentStatus = switch (status) {
				case CONFIRMED, COMPLETED -> PaymentStatus.PAID;
				case CANCELLED -> PaymentStatus.FAILED;
				default -> PaymentStatus.PENDING;
			};

			paymentRepository.save(Payment.builder()
					.booking(booking)
					.amount(total)
					.currency("VND")
					.transactionRef(ref)
					.qrCodeUrl("https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=" + ref)
					.status(paymentStatus)
					.createdAt(booking.getCreatedAt())
					.paidAt(paymentStatus == PaymentStatus.PAID ? booking.getCreatedAt().plus(1, ChronoUnit.HOURS) : null)
					.build());
		}
	}
}
