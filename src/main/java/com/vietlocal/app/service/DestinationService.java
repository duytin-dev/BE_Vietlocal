package com.vietlocal.app.service;

import com.vietlocal.app.dto.response.DestinationResponse;
import com.vietlocal.app.dto.response.PageResponse;
import com.vietlocal.app.exception.ResourceNotFoundException;
import com.vietlocal.app.repository.DestinationRepository;
import com.vietlocal.app.utils.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DestinationService {

	private final DestinationRepository destinationRepository;

	@Transactional(readOnly = true)
	public PageResponse<DestinationResponse> findAll(Pageable pageable) {
		Page<DestinationResponse> page = destinationRepository.findAll(pageable)
				.map(EntityMapper::toDestinationResponse);
		return PageResponse.from(page);
	}

	@Transactional(readOnly = true)
	public DestinationResponse findBySlug(String slug) {
		return destinationRepository.findBySlug(slug)
				.map(EntityMapper::toDestinationResponse)
				.orElseThrow(() -> new ResourceNotFoundException("Destination not found: " + slug));
	}
}
