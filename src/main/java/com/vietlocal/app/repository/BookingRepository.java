package com.vietlocal.app.repository;

import com.vietlocal.app.domain.entity.Booking;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	@Query("SELECT b FROM Booking b JOIN FETCH b.guide WHERE b.id = :id")
	Optional<Booking> findByIdWithGuide(@Param("id") Long id);

	@Query(value = "SELECT b FROM Booking b LEFT JOIN FETCH b.guide",
			countQuery = "SELECT COUNT(b) FROM Booking b")
	Page<Booking> findAllForAdmin(Pageable pageable);

	long countByStatus(com.vietlocal.app.domain.enums.BookingStatus status);

	@Query("""
			SELECT b.user.id, COUNT(b)
			FROM Booking b
			WHERE b.user IS NOT NULL
			GROUP BY b.user.id
			""")
	List<Object[]> countBookingsGroupByUserId();

	@Query("""
			SELECT b FROM Booking b
			JOIN FETCH b.guide
			LEFT JOIN FETCH b.payment
			WHERE b.user.id = :userId
			AND b.notificationDismissed = false
			ORDER BY b.createdAt DESC
			""")
	List<Booking> findAllByUserIdWithDetails(Long userId);

	@Query("""
			SELECT b FROM Booking b
			LEFT JOIN FETCH b.payment
			LEFT JOIN FETCH b.user
			WHERE b.id = :id
			""")
	Optional<Booking> findByIdWithPaymentAndUser(Long id);

	@Query("""
			SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
			FROM Booking b
			WHERE b.guide.id = :guideId
			AND b.status IN (com.vietlocal.app.domain.enums.BookingStatus.PENDING, com.vietlocal.app.domain.enums.BookingStatus.CONFIRMED)
			""")
	boolean existsActiveBookingByGuideId(Long guideId);

	@Query("""
			SELECT DISTINCT b.guide.id FROM Booking b
			WHERE b.guide IS NOT NULL
			AND b.status IN (com.vietlocal.app.domain.enums.BookingStatus.PENDING, com.vietlocal.app.domain.enums.BookingStatus.CONFIRMED)
			""")
	List<Long> findGuideIdsWithActiveBookings();
}
