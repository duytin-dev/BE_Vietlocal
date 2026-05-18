package com.vietlocal.app.service;

import com.vietlocal.app.domain.enums.GuideTier;
import com.vietlocal.app.dto.response.GuideResponse;
import com.vietlocal.app.dto.response.PageResponse;
import com.vietlocal.app.exception.ResourceNotFoundException;
import com.vietlocal.app.repository.BookingRepository;
import com.vietlocal.app.repository.GuideRepository;
import com.vietlocal.app.utils.EntityMapper;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GuideService {

	private final GuideRepository guideRepository;
	private final BookingRepository bookingRepository;

	@Transactional(readOnly = true)
	public PageResponse<GuideResponse> findAll(GuideTier tier, Pageable pageable) {
		Set<Long> busyGuideIds = new HashSet<>(bookingRepository.findGuideIdsWithActiveBookings());
		Page<GuideResponse> page = tier != null
				? guideRepository.findByTierOrderByRatingDesc(tier, pageable)
						.map(g -> EntityMapper.toGuideResponse(g, !busyGuideIds.contains(g.getId())))
				: guideRepository.findAllByOrderByRatingDesc(pageable)
						.map(g -> EntityMapper.toGuideResponse(g, !busyGuideIds.contains(g.getId())));
		return PageResponse.from(page);
	}

	@Transactional(readOnly = true)
	public GuideResponse findById(Long id) {
		return guideRepository
				.findById(id)
				.map(g -> EntityMapper.toGuideResponse(g, !bookingRepository.existsActiveBookingByGuideId(id)))
				.orElseThrow(() -> new ResourceNotFoundException("Guide not found: " + id));
	}
}
