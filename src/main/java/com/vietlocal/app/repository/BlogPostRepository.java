package com.vietlocal.app.repository;

import com.vietlocal.app.domain.entity.BlogPost;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

	@EntityGraph(attributePaths = "destination")
	Page<BlogPost> findAll(Pageable pageable);

	@Query("SELECT b FROM BlogPost b LEFT JOIN FETCH b.destination WHERE b.slug = :slug")
	Optional<BlogPost> findBySlugWithDestination(@Param("slug") String slug);
}
