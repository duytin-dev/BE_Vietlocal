package com.vietlocal.app.repository;

import com.vietlocal.app.domain.entity.DestinationTrip;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DestinationTripRepository extends JpaRepository<DestinationTrip, Long> {

	@Query("""
			SELECT t FROM DestinationTrip t
			JOIN FETCH t.destination d
			ORDER BY d.name ASC, t.sortOrder ASC, t.title ASC
			""")
	List<DestinationTrip> findAllWithDestinationOrdered();
}
