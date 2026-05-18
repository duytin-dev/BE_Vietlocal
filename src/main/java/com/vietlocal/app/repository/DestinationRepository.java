package com.vietlocal.app.repository;

import com.vietlocal.app.domain.entity.Destination;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinationRepository extends JpaRepository<Destination, Long> {

	Optional<Destination> findBySlug(String slug);

	Page<Destination> findByFeaturedTrueOrderByNameAsc(Pageable pageable);

	List<Destination> findAllByOrderByNameAsc();
}
