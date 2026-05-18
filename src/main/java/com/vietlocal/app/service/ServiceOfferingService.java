package com.vietlocal.app.service;

import com.vietlocal.app.dto.response.ServiceResponse;
import com.vietlocal.app.exception.ResourceNotFoundException;
import com.vietlocal.app.repository.ServiceOfferingRepository;
import com.vietlocal.app.utils.EntityMapper;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ServiceOfferingService {

	private final ServiceOfferingRepository serviceOfferingRepository;

	@Transactional(readOnly = true)
	public List<ServiceResponse> findAll() {
		return serviceOfferingRepository.findAll().stream()
				.sorted(Comparator.comparingInt(com.vietlocal.app.domain.entity.ServiceOffering::getSortOrder))
				.map(EntityMapper::toServiceResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public ServiceResponse findById(Long id) {
		return serviceOfferingRepository.findById(id)
				.map(EntityMapper::toServiceResponse)
				.orElseThrow(() -> new ResourceNotFoundException("Service not found: " + id));
	}
}
