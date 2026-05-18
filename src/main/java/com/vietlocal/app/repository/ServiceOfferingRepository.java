package com.vietlocal.app.repository;

import com.vietlocal.app.domain.entity.ServiceOffering;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, Long> {

	Optional<ServiceOffering> findBySlug(String slug);
}
