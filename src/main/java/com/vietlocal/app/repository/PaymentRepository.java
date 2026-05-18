package com.vietlocal.app.repository;

import com.vietlocal.app.domain.entity.Payment;
import com.vietlocal.app.domain.enums.PaymentStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	long countByStatus(PaymentStatus status);

	@Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = :status")
	BigDecimal sumAmountByStatus(@Param("status") PaymentStatus status);

	@Query("SELECT p FROM Payment p JOIN FETCH p.booking b JOIN FETCH b.guide WHERE b.id = :bookingId")
	Optional<Payment> findByBookingIdWithDetails(@Param("bookingId") Long bookingId);

	List<Payment> findByBooking_IdIn(List<Long> bookingIds);
}
