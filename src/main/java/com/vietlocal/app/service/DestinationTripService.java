package com.vietlocal.app.service;

import com.vietlocal.app.domain.entity.DestinationTrip;
import com.vietlocal.app.dto.response.DestinationTripResponse;
import com.vietlocal.app.repository.DestinationTripRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DestinationTripService {

	private final DestinationTripRepository destinationTripRepository;

	@Transactional(readOnly = true)
	public List<DestinationTripResponse> findAll() {
		return destinationTripRepository.findAllWithDestinationOrdered().stream()
				.map(this::toResponse)
				.toList();
	}

	private DestinationTripResponse toResponse(DestinationTrip trip) {
		return DestinationTripResponse.builder()
				.id(trip.getId())
				.destinationId(trip.getDestination().getId())
				.destinationName(trip.getDestination().getName())
				.destinationSlug(trip.getDestination().getSlug())
				.title(trip.getTitle())
				.summary(trip.getSummary())
				.durationDays(trip.getDurationDays())
				.build();
	}
}
