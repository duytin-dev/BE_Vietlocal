package com.vietlocal.app.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class AdminAuthFilter extends OncePerRequestFilter {

	private final AppProperties appProperties;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return !request.getRequestURI().startsWith("/api/admin");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String secret = request.getHeader("X-Admin-Secret");
		if (secret == null || !secret.equals(appProperties.getAdmin().getSecret())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write(
					"{\"success\":false,\"errorCode\":\"UNAUTHORIZED\",\"message\":\"Invalid or missing X-Admin-Secret\"}");
			return;
		}
		filterChain.doFilter(request, response);
	}
}
