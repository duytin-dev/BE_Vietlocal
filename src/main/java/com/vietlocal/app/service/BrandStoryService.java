package com.vietlocal.app.service;

import com.vietlocal.app.dto.response.BrandStoryResponse;
import com.vietlocal.app.exception.ResourceNotFoundException;
import com.vietlocal.app.repository.BrandStoryRepository;
import com.vietlocal.app.utils.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BrandStoryService {

	private final BrandStoryRepository brandStoryRepository;

	@Transactional(readOnly = true)
	public BrandStoryResponse getAbout() {
		return brandStoryRepository.findAll().stream()
				.findFirst()
				.map(EntityMapper::toBrandStoryResponse)
				.orElseThrow(() -> new ResourceNotFoundException("Brand story not found"));
	}
}
