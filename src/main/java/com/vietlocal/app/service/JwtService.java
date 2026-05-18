package com.vietlocal.app.service;

import com.vietlocal.app.config.AppProperties;
import com.vietlocal.app.exception.BusinessException;
import com.vietlocal.app.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	private final SecretKey key;
	private final long expirationMs;

	public JwtService(AppProperties appProperties) {
		String secret = appProperties.getAuth().getJwt().getSecret();
		if (secret == null || secret.length() < 32) {
			throw new IllegalStateException("app.auth.jwt.secret must be at least 32 characters");
		}
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expirationMs = appProperties.getAuth().getJwt().getExpirationMs();
	}

	public String createToken(Long userId, String email) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + expirationMs);
		return Jwts.builder()
				.subject(email)
				.claim("uid", userId)
				.issuedAt(now)
				.expiration(expiry)
				.signWith(key)
				.compact();
	}

	public Long getUserId(String token) {
		Claims claims = parseClaims(token);
		Number uid = claims.get("uid", Number.class);
		if (uid == null) {
			throw new BusinessException(ErrorCode.UNAUTHORIZED, "Invalid token");
		}
		return uid.longValue();
	}

	public String getEmail(String token) {
		return parseClaims(token).getSubject();
	}

	private Claims parseClaims(String token) {
		try {
			return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
		} catch (Exception ex) {
			throw new BusinessException(ErrorCode.UNAUTHORIZED, "Invalid or expired token");
		}
	}
}
