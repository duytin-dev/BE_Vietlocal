package com.vietlocal.app.repository;

import com.vietlocal.app.domain.entity.Guide;
import com.vietlocal.app.domain.enums.GuideTier;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GuideRepository extends JpaRepository<Guide, Long> {

	Page<Guide> findByTierOrderByRatingDesc(GuideTier tier, Pageable pageable);

	Page<Guide> findAllByOrderByRatingDesc(Pageable pageable);

	Optional<Guide> findBySlug(String slug);

	List<Guide> findTop3ByOrderByRatingDesc();

	@Query("""
			SELECT g FROM Guide g
			WHERE NOT EXISTS (
			    SELECT 1 FROM Booking b
			    WHERE b.guide = g
			    AND b.status IN (
			        com.vietlocal.app.domain.enums.BookingStatus.PENDING,
			        com.vietlocal.app.domain.enums.BookingStatus.CONFIRMED
			    )
			)
			ORDER BY g.rating DESC
			""")
	List<Guide> findAvailableOrderByRatingDesc(Pageable pageable);

	boolean existsBySlug(String slug);
}
