package com.vietlocal.app.service;

import com.vietlocal.app.domain.entity.Destination;
import com.vietlocal.app.domain.entity.DestinationFavorite;
import com.vietlocal.app.domain.entity.User;
import com.vietlocal.app.dto.response.DestinationResponse;
import com.vietlocal.app.exception.ResourceNotFoundException;
import com.vietlocal.app.repository.DestinationFavoriteRepository;
import com.vietlocal.app.repository.DestinationRepository;
import com.vietlocal.app.repository.UserRepository;
import com.vietlocal.app.utils.EntityMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

	private final DestinationFavoriteRepository favoriteRepository;
	private final DestinationRepository destinationRepository;
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public List<DestinationResponse> listForUser(Long userId) {
		return favoriteRepository.findByUserIdWithDestination(userId).stream()
				.map(DestinationFavorite::getDestination)
				.map(EntityMapper::toDestinationResponse)
				.toList();
	}

	@Transactional
	public void add(Long userId, Long destinationId) {
		if (favoriteRepository.existsByUser_IdAndDestination_Id(userId, destinationId)) {
			return;
		}
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		Destination destination = destinationRepository.findById(destinationId)
				.orElseThrow(() -> new ResourceNotFoundException("Destination not found"));
		favoriteRepository.save(DestinationFavorite.builder()
				.user(user)
				.destination(destination)
				.build());
	}

	@Transactional
	public void remove(Long userId, Long destinationId) {
		favoriteRepository.deleteByUser_IdAndDestination_Id(userId, destinationId);
	}
}
