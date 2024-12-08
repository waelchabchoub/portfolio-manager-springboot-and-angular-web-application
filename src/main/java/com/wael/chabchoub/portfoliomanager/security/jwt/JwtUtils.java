package com.wael.chabchoub.portfoliomanager.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.wael.chabchoub.portfoliomanager.security.config.PortfolioManagerProperties;

import java.util.Date;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class JwtUtils {
	private final long EXPIRATION_TIME = 86400000; // 1 day
	private final PortfolioManagerProperties portfolioManagerProperties;

	public String generateToken(String login) {
		return Jwts.builder().setSubject(login).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).signWith(getKey()).compact();
	}

	public String extractLogin(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException e) {
			return false;
		}
	}

	private SecretKey getKey() {
		return Keys.hmacShaKeyFor(portfolioManagerProperties.getJwt().getSecret().getBytes());
	}
}
