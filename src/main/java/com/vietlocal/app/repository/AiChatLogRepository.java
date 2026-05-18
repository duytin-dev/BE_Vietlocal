package com.vietlocal.app.repository;

import com.vietlocal.app.domain.entity.AiChatLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiChatLogRepository extends JpaRepository<AiChatLog, Long> {

	Page<AiChatLog> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
