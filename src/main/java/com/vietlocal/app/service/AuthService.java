package com.vietlocal.app.service;

import com.vietlocal.app.domain.entity.User;
import com.vietlocal.app.domain.enums.UserRole;
import com.vietlocal.app.dto.request.LoginRequest;
import com.vietlocal.app.dto.request.RegisterRequest;
import com.vietlocal.app.dto.response.AuthResponse;
import com.vietlocal.app.dto.response.UserResponse;
import com.vietlocal.app.exception.BusinessException;
import com.vietlocal.app.exception.ErrorCode;
import com.vietlocal.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	@Transactional
	public AuthResponse register(RegisterRequest request) {
		String email = normalizeEmail(request.email());
		if (userRepository.existsByEmailIgnoreCase(email)) {
			throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, "Email is already registered");
		}
		User user = User.builder()
				.email(email)
				.passwordHash(passwordEncoder.encode(request.password()))
				.fullName(request.fullName().trim())
				.role(UserRole.USER)
				.build();
		user = userRepository.save(user);
		return toAuthResponse(user);
	}

	public AuthResponse login(LoginRequest request) {
		String email = normalizeEmail(request.email());
		User user = userRepository
				.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS, "Invalid email or password"));
		if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
			throw new BusinessException(ErrorCode.INVALID_CREDENTIALS, "Invalid email or password");
		}
		return toAuthResponse(user);
	}

	public UserResponse getProfile(Long userId) {
		User user = userRepository
				.findById(userId)
				.orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "User not found"));
		return UserResponse.from(user);
	}

	private AuthResponse toAuthResponse(User user) {
		String token = jwtService.createToken(user.getId(), user.getEmail());
		return new AuthResponse(token, UserResponse.from(user));
	}

	private static String normalizeEmail(String email) {
		return email.trim().toLowerCase();
	}
}
