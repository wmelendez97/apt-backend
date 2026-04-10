package com.apt.api.token;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.expiration}")
	private long expirationTime;

	// Gets HS256 signing key
	private SecretKey getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	// Extracts all claims from token
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}

	// Extracts a specific claim
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// Gets the user (subject)
	public String extractUser(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// Validates token expiration
	private boolean isTokenExpired(String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}

	// Validates token (signature + expiration)
	public boolean validateToken(String token) {
		try {
			return !isTokenExpired(token);
		} catch (Exception e) {
			return false;
		}
	}

	// Generates JWT for authenticated user
	public String generateToken(String user) {
		Date now = new Date();
		Date expirationDate = new Date(now.getTime() + expirationTime);

		return Jwts.builder().setSubject(user).setIssuedAt(now).setExpiration(expirationDate)
				.signWith(getSigningKey()).compact();
	}
}