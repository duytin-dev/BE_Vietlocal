package com.vietlocal.app.service;

import com.vietlocal.app.domain.entity.Booking;
import com.vietlocal.app.domain.entity.Guide;
import com.vietlocal.app.domain.entity.Payment;
import com.vietlocal.app.domain.entity.User;
import com.vietlocal.app.dto.response.BookingNotificationResponse;
import com.vietlocal.app.domain.enums.BookingStatus;
import com.vietlocal.app.domain.enums.PaymentStatus;
import com.vietlocal.app.dto.request.CreateBookingRequest;
import com.vietlocal.app.dto.response.BookingResponse;
import com.vietlocal.app.dto.response.PaymentQrResponse;
import com.vietlocal.app.exception.BusinessException;
import com.vietlocal.app.exception.ErrorCode;
import com.vietlocal.app.exception.ResourceNotFoundException;
import com.vietlocal.app.repository.BookingRepository;
import com.vietlocal.app.repository.GuideRepository;
import com.vietlocal.app.repository.PaymentRepository;
import com.vietlocal.app.repository.UserRepository;
import java.util.List;
import com.vietlocal.app.utils.EntityMapper;
import com.vietlocal.app.utils.TripTitleHelper;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingService {

	private final BookingRepository bookingRepository;
	private final GuideRepository guideRepository;
	private final PaymentRepository paymentRepository;
	private final UserRepository userRepository;
	private final PricingService pricingService;

	@Transactional
	public BookingResponse create(CreateBookingRequest request, Long userId) {
		Guide guide = guideRepository.findById(request.getGuideId())
				.orElseThrow(() -> new ResourceNotFoundException("Guide not found: " + request.getGuideId()));

		if (bookingRepository.existsActiveBookingByGuideId(guide.getId())) {
			throw new BusinessException(
					ErrorCode.GUIDE_NOT_AVAILABLE,
					"Guide is already assigned to an active tour. Choose another guide or wait until the current tour ends.");
		}

		int days = request.getEstimatedDays();
		String tripTitle = request.getTripTitle();
		if (tripTitle == null || tripTitle.isBlank()) {
			tripTitle = TripTitleHelper.build(request.getDestinationName(), guide.getName());
		} else {
			tripTitle = tripTitle.trim();
		}
		BigDecimal totalAmount = pricingService.calculateTotal(guide, days);

		User user = null;
		if (userId != null) {
			user = userRepository.findById(userId)
					.orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
		}

		Booking booking = Booking.builder()
				.user(user)
				.customerName(request.getCustomerName())
				.email(request.getEmail())
				.phone(request.getPhone())
				.guide(guide)
				.destinationName(request.getDestinationName())
				.tripTitle(tripTitle)
				.itinerarySummary(request.getItinerarySummary())
				.customerNotes(request.getCustomerNotes())
				.estimatedDays(days)
				.totalAmount(totalAmount)
				.status(BookingStatus.PENDING)
				.build();
		booking = bookingRepository.save(booking);

		String transactionRef = "VL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
		Payment payment = Payment.builder()
				.booking(booking)
				.amount(totalAmount)
				.currency("VND")
				.transactionRef(transactionRef)
				.qrCodeUrl("https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=VietLocal-" + transactionRef)
				.status(PaymentStatus.PENDING)
				.build();
		paymentRepository.save(payment);

		return EntityMapper.toBookingResponse(booking);
	}

	@Transactional(readOnly = true)
	public BookingResponse findById(Long id) {
		Booking booking = bookingRepository.findByIdWithGuide(id)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + id));
		return EntityMapper.toBookingResponse(booking);
	}

	@Transactional(readOnly = true)
	public PaymentQrResponse getPaymentQr(Long bookingId) {
		Payment payment = paymentRepository.findByBookingIdWithDetails(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking: " + bookingId));
		return EntityMapper.toPaymentQrResponse(payment);
	}

	@Transactional
	public PaymentQrResponse confirmPayment(Long bookingId) {
		Payment payment = paymentRepository.findByBookingIdWithDetails(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking: " + bookingId));

		if (payment.getStatus() == PaymentStatus.PAID) {
			throw new BusinessException(ErrorCode.PAYMENT_ALREADY_PAID, "Payment already completed");
		}
		if (payment.getStatus() != PaymentStatus.PENDING) {
			throw new BusinessException(ErrorCode.PAYMENT_NOT_PENDING,
					"Payment cannot be confirmed in status: " + payment.getStatus());
		}

		payment.setStatus(PaymentStatus.PAID);
		payment.setPaidAt(Instant.now());
		paymentRepository.save(payment);

		Booking booking = payment.getBooking();
		booking.setStatus(BookingStatus.CONFIRMED);
		bookingRepository.save(booking);

		return EntityMapper.toPaymentQrResponse(payment);
	}

	@Transactional(readOnly = true)
	public List<BookingNotificationResponse> findNotificationsForUser(Long userId) {
		return bookingRepository.findAllByUserIdWithDetails(userId).stream()
				.map(this::toNotification)
				.toList();
	}

	@Transactional
	public void dismissNotification(Long userId, Long bookingId) {
		Booking booking = bookingRepository
				.findByIdWithPaymentAndUser(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));
		if (booking.getUser() == null || !booking.getUser().getId().equals(userId)) {
			throw new ResourceNotFoundException("Booking not found: " + bookingId);
		}
		PaymentStatus paymentStatus = booking.getPayment() != null
				? booking.getPayment().getStatus()
				: PaymentStatus.PENDING;
		if (paymentStatus != PaymentStatus.PAID) {
			throw new BusinessException(
					ErrorCode.VALIDATION_ERROR, "Only paid trips can be removed from notifications");
		}
		booking.setNotificationDismissed(true);
		booking.setStatus(BookingStatus.COMPLETED);
		bookingRepository.save(booking);
	}

	private BookingNotificationResponse toNotification(Booking booking) {
		String tripName = resolveTripTitle(booking);
		PaymentStatus paymentStatus = booking.getPayment() != null
				? booking.getPayment().getStatus()
				: PaymentStatus.PENDING;
		return BookingNotificationResponse.builder()
				.bookingId(booking.getId())
				.customerName(booking.getCustomerName())
				.tripName(tripName)
				.totalAmount(booking.getTotalAmount())
				.paymentStatus(paymentStatus)
				.createdAt(booking.getCreatedAt())
				.build();
	}

	private static String resolveTripTitle(Booking booking) {
		if (booking.getTripTitle() != null && !booking.getTripTitle().isBlank()) {
			return booking.getTripTitle();
		}
		String guideName = booking.getGuide() != null ? booking.getGuide().getName() : "HDV";
		return TripTitleHelper.build(booking.getDestinationName(), guideName);
	}
}
