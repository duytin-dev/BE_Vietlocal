package com.vietlocal.app.domain.entity;

import com.vietlocal.app.domain.enums.BookingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String customerName;

	@Column(nullable = false)
	private String email;

	private String phone;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "guide_id")
	private Guide guide;

	@Column(name = "destination_name")
	private String destinationName;

	@Column(name = "trip_title")
	private String tripTitle;

	@OneToOne(mappedBy = "booking", fetch = FetchType.LAZY)
	private Payment payment;

	@Column(columnDefinition = "TEXT")
	private String itinerarySummary;

	@Column(columnDefinition = "TEXT")
	private String customerNotes;

	@Enumerated(EnumType.STRING)
	@Builder.Default
	private BookingStatus status = BookingStatus.PENDING;

	@Column(name = "estimated_days", nullable = false)
	@Builder.Default
	private int estimatedDays = 1;

	@Column(name = "total_amount", nullable = false)
	private BigDecimal totalAmount;

	@Column(name = "notification_dismissed", nullable = false)
	@Builder.Default
	private boolean notificationDismissed = false;

	@Builder.Default
	private Instant createdAt = Instant.now();
}
