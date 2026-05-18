package com.vietlocal.app.service;

import com.vietlocal.app.domain.entity.AiChatLog;
import com.vietlocal.app.domain.entity.Booking;
import com.vietlocal.app.domain.entity.Payment;
import com.vietlocal.app.domain.entity.User;
import com.vietlocal.app.domain.enums.BookingStatus;
import com.vietlocal.app.domain.enums.PaymentStatus;
import com.vietlocal.app.dto.response.AdminAiChatLogResponse;
import com.vietlocal.app.dto.response.AdminBookingResponse;
import com.vietlocal.app.dto.response.AdminDashboardResponse;
import com.vietlocal.app.dto.response.AdminUserResponse;
import com.vietlocal.app.dto.response.PageResponse;
import com.vietlocal.app.repository.AiChatLogRepository;
import com.vietlocal.app.repository.BookingRepository;
import com.vietlocal.app.repository.GuideRepository;
import com.vietlocal.app.repository.PaymentRepository;
import com.vietlocal.app.repository.UserRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final BookingRepository bookingRepository;
	private final PaymentRepository paymentRepository;
	private final AiChatLogRepository aiChatLogRepository;
	private final UserRepository userRepository;
	private final GuideRepository guideRepository;

	@Transactional(readOnly = true)
	public AdminDashboardResponse getDashboard() {
		long pendingPayment = paymentRepository.countByStatus(PaymentStatus.PENDING);
		long paid = paymentRepository.countByStatus(PaymentStatus.PAID);
		BigDecimal revenue = paymentRepository.sumAmountByStatus(PaymentStatus.PAID);
		if (revenue == null) {
			revenue = BigDecimal.ZERO;
		}
		return AdminDashboardResponse.builder()
				.totalBookings(bookingRepository.count())
				.pendingPaymentBookings(pendingPayment)
				.paidBookings(paid)
				.totalRevenue(revenue)
				.totalUsers(userRepository.count())
				.totalAiChats(aiChatLogRepository.count())
				.totalGuides(guideRepository.count())
				.build();
	}

	@Transactional(readOnly = true)
	public PageResponse<AdminBookingResponse> listBookings(Pageable pageable) {
		Page<Booking> page = bookingRepository.findAllForAdmin(pageable);
		List<Long> bookingIds = page.getContent().stream().map(Booking::getId).toList();
		Map<Long, Payment> paymentsByBooking = bookingIds.isEmpty()
				? Map.of()
				: paymentRepository.findByBooking_IdIn(bookingIds).stream()
						.collect(Collectors.toMap(p -> p.getBooking().getId(), p -> p));

		Page<AdminBookingResponse> mapped = page.map(b -> toAdminBooking(b, paymentsByBooking.get(b.getId())));
		return PageResponse.from(mapped);
	}

	@Transactional(readOnly = true)
	public PageResponse<AdminUserResponse> listUsers(Pageable pageable) {
		Map<Long, Long> bookingCounts = bookingRepository.countBookingsGroupByUserId().stream()
				.collect(Collectors.toMap(row -> (Long) row[0], row -> (Long) row[1]));
		Page<AdminUserResponse> page = userRepository.findAllByOrderByCreatedAtDesc(pageable)
				.map(u -> toAdminUser(u, bookingCounts.getOrDefault(u.getId(), 0L)));
		return PageResponse.from(page);
	}

	@Transactional(readOnly = true)
	public PageResponse<AdminAiChatLogResponse> listAiChats(Pageable pageable) {
		Page<AdminAiChatLogResponse> page = aiChatLogRepository.findAllByOrderByCreatedAtDesc(pageable)
				.map(this::toAdminAiChat);
		return PageResponse.from(page);
	}

	private AdminUserResponse toAdminUser(User u, long bookingCount) {
		return AdminUserResponse.builder()
				.id(u.getId())
				.email(u.getEmail())
				.fullName(u.getFullName())
				.role(u.getRole())
				.createdAt(u.getCreatedAt())
				.bookingCount(bookingCount)
				.build();
	}

	private AdminBookingResponse toAdminBooking(Booking b, Payment payment) {
		User user = b.getUser();
		return AdminBookingResponse.builder()
				.id(b.getId())
				.customerName(b.getCustomerName())
				.email(b.getEmail())
				.phone(b.getPhone())
				.userId(user != null ? user.getId() : null)
				.userFullName(user != null ? user.getFullName() : null)
				.guideId(b.getGuide() != null ? b.getGuide().getId() : null)
				.guideName(b.getGuide() != null ? b.getGuide().getName() : null)
				.destinationName(b.getDestinationName())
				.tripTitle(b.getTripTitle())
				.itinerarySummary(b.getItinerarySummary())
				.customerNotes(b.getCustomerNotes())
				.estimatedDays(b.getEstimatedDays())
				.totalAmount(b.getTotalAmount())
				.status(b.getStatus())
				.createdAt(b.getCreatedAt())
				.paymentStatus(payment != null ? payment.getStatus() : null)
				.transactionRef(payment != null ? payment.getTransactionRef() : null)
				.build();
	}

	private AdminAiChatLogResponse toAdminAiChat(AiChatLog log) {
		List<Long> guideIds = List.of();
		if (log.getSuggestedGuideIds() != null && !log.getSuggestedGuideIds().isBlank()) {
			guideIds = Arrays.stream(log.getSuggestedGuideIds().split(","))
					.map(String::trim)
					.filter(s -> !s.isEmpty())
					.map(Long::parseLong)
					.toList();
		}
		return AdminAiChatLogResponse.builder()
				.id(log.getId())
				.sessionId(log.getSessionId())
				.userMessage(log.getUserMessage())
				.aiReply(log.getAiReply())
				.suggestedItinerary(log.getSuggestedItinerary())
				.suggestedGuideIds(guideIds)
				.createdAt(log.getCreatedAt())
				.build();
	}
}
