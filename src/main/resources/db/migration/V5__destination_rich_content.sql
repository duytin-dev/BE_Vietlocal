-- Cập nhật mô tả chi tiết cho DB đã seed trước đó (DataInitializer chỉ chạy khi DB trống)

UPDATE destinations SET
  summary = 'Thành phố biển năng động: cầu Rồng, Bà Nà, Mỹ Khê và ẩm thực đường phố đậm chất miền Trung.',
  description = 'Đà Nẵng là điểm đến lý tưởng cho du lịch kết hợp biển, núi và văn hóa. Buổi tối nên xem cầu Rồng phun lửa (cuối tuần), tham quan Bà Nà Hills với Cầu Vàng, tắm biển Mỹ Khê hoặc Non Nước. Ẩm thực nổi bật: mì Quảng, bánh tráng cuốn thịt heo, bún chả cá, hải sản chợ đêm Helio. Thời điểm đẹp: tháng 3–8 (ít mưa). Gợi ý lưu trú quanh biển Sơn Trà hoặc trung tâm Hải Châu. VietLocal có hướng dẫn viên địa phương hỗ trợ tour ngắn ngày, food tour và trải nghiệm làng nghề.'
WHERE slug = 'da-nang';

UPDATE destinations SET
  summary = 'Thủ đô nghìn năm văn hiến: Hồ Gươm, phố cổ, ẩm thực đường phố và không gian cà phê vintage.',
  description = 'Hà Nội mang vẻ đẹp cổ kính xen lẫn nhịp sống hiện đại. Đi bộ quanh Hồ Hoàn Kiếm, Văn Miếu, Lăng Bác, Phố cổ 36 phố phường; buổi tối thưởng thức bia hơi Ta Hien. Món nên thử: phở bò, bún chả, chả cá Lã Vọng, bánh cuốn Thanh Trì, cốm Vòng. Mùa thu (tháng 9–11) và xuân (tháng 2–4) là thời điểm dễ chịu nhất. Có thể kết hợp Ninh Bình hoặc Hạ Long từ Hà Nội. VietLocal kết nối guide am hiểu lịch sử, ẩm thực và văn hóa địa phương.'
WHERE slug = 'ha-noi';

UPDATE destinations SET
  summary = 'Vùng núi cao Tây Bắc: ruộng bậc thang, Fansipan, bản làng dân tộc và khí hậu mát quanh năm.',
  description = 'Sa Pa thu hút du khách bởi cảnh quan núi non hùng vĩ và văn hóa các dân tộc (H''Mông, Dao, Tày…). Trekking ruộng bậc thang, chinh phục Fansipan, homestay Lao Chải, Ta Van. Chợ phiên cuối tuần, nên mang áo ấm; mùa lúa chín tháng 9–10 rất đẹp. Ẩm thực: thịt trâu gác bếp, cá hồi, rượu ngô. VietLocal có guide bản địa dẫn trek an toàn.'
WHERE slug = 'sa-pa';

UPDATE destinations SET
  summary = 'Phố cổ UNESCO: đèn lồng, sông Thu Bồn, làng gốm Thanh Hà và ẩm thực cao lầu.',
  description = 'Hội An là điểm đến lãng mạn và văn hóa, đặc biệt về đêm khi phố cổ thắp đèn lồng. Tham quan Chùa Cầu, nhà cổ, làng rau Trà Quế, làng gốm Thanh Hà, thả đèn hoa đăng sông Hoài. Gần có biển An Bàng, Cửa Đại. Đặc sản: cao lầu, mì Quảng, bánh mì Phượng. Nên đi tháng 2–4 hoặc 8–10. VietLocal hỗ trợ tour phố cổ, nấu ăn và làng nghề.'
WHERE slug = 'hoi-an';

UPDATE destinations SET
  summary = 'Cố đô triều Nguyễn: Đại Nội, lăng tẩm, sông Hương và ẩm thực cung đình tinh tế.',
  description = 'Huế mang dấu ấn lịch sử với Đại Nội, lăng Khải Định, lăng Tự Đức, chùa Thiên Mụ. Du thuyền sông Hương, nghe ca Huế. Ẩm thực: bún bò Huế, cơm hến, bánh bèo, chè Huế. Phù hợp du lịch chậm 2–3 ngày; kết hợp Lăng Cô khi đi Đà Nẵng–Huế. Mùa đẹp: tháng 1–4. VietLocal có guide am hiểu lịch sử triều Nguyễn.'
WHERE slug = 'hue';

UPDATE destinations SET
  summary = 'Thành phố biển nhiệt đới: VinWonders, lặn san hô, đảo và hải sản tươi sống.',
  description = 'Nha Trang nổi bật với bãi biển dài, nắng đẹp quanh năm. Tham quan VinWonders, Tháp Bà Ponagar, Viện Hải dương học; tour đảo Hòn Mun, Hòn Tằm lặn san hô. Ẩm thực: nem nướng Ninh Hòa, bún cá, hải sản chợ Đầm. Thời điểm tốt: tháng 2–8. Phù hợp gia đình và nghỉ dưỡng. VietLocal hỗ trợ tour đảo và snorkeling.'
WHERE slug = 'nha-trang';

UPDATE destinations SET
  summary = 'Đảo ngọc Kiên Giang: bãi Sao, Grand World, nước mắm, hải sản và hoàng hôn trên biển.',
  description = 'Phú Quốc là thiên đường nghỉ dưỡng với bãi trong xanh và resort cao cấp. Ghé bãi Sao, bãi Dài, Grand World, chợ đêm Dinh Cậu, nhà thùng nước mắm. Hoạt động: lặn, câu mực đêm, cáp treo Hòn Thơm. Đặc sản: gỏi cá trích, hải sản. Mùa khô tháng 11–4 thuận tiện. VietLocal có guide tour bãi biển và làng chài.'
WHERE slug = 'phu-quoc';

UPDATE destinations SET
  summary = 'Thành phố ngàn hoa trên cao nguyên: thác Datanla, đồi chè, cafe và không khí se lạnh.',
  description = 'Đà Lạt mang khí hậu mát quanh năm. Điểm tham quan: Hồ Xuân Hương, Thung lũng Tình yêu, Dinh Bảo Đại, thác Datanla, Prenn, đồi chè Cầu Đất. Ẩm thực: bánh căn, bánh tráng nướng, lẩu gà lá é, dâu tây. Mùa hoa dã quỳ tháng 10–11. Nên thuê xe máy khám phá. VietLocal hỗ trợ tour săn mây và homestay vườn hoa.'
WHERE slug = 'da-lat';

UPDATE destinations SET
  summary = 'Trung tâm Đồng bằng sông Cửu Long: chợ nổi Cái Răng, miệt vườn và văn hóa sông nước.',
  description = 'Cần Thơ là cửa ngõ miền Tây với chợ nổi Cái Răng (nên đi 5–7h sáng), chùa Ông, bến Ninh Kiều, Mỹ Khánh. Trải nghiệm: chèo sampan, đờn ca tài tử, lẩu mắm, bánh pía. Thích hợp tour 1–2 ngày từ TP.HCM. Mùa nước nổi tháng 8–11 đặc sắc. VietLocal có guide dẫn chợ nổi và miệt vườn.'
WHERE slug = 'can-tho';

UPDATE destinations SET
  summary = 'Vịnh di sản thế giới: đảo đá vôi, du thuyền qua đêm và kayak giữa hang động kỳ vĩ.',
  description = 'Vịnh Hạ Long là biểu tượng du lịch Việt Nam với hàng nghìn đảo đá vôi. Tour 1–2 ngày: du thuyền, kayak hang Luồn, Sửng Sốt, tắm biển Titop, làng chài Cửa Vạn. Kết hợp Yên Tử, Cát Bà. Ẩm thực: hải sản, chả mực Hạ Long. Mùa đẹp: tháng 10–4. VietLocal hỗ trợ tư vấn tour tại cảng Tuần Châu, Hòn Gai.'
WHERE slug = 'ha-long';
