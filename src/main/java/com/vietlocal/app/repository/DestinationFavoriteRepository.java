package com.vietlocal.app.repository;

import com.vietlocal.app.domain.entity.DestinationFavorite;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DestinationFavoriteRepository extends JpaRepository<DestinationFavorite, Long> {

	@Query("""
			SELECT f FROM DestinationFavorite f
			JOIN FETCH f.destination
			WHERE f.user.id = :userId
			ORDER BY f.createdAt DESC
			""")
	List<DestinationFavorite> findByUserIdWithDestination(@Param("userId") Long userId);

	boolean existsByUser_IdAndDestination_Id(Long userId, Long destinationId);

	void deleteByUser_IdAndDestination_Id(Long userId, Long destinationId);
}
