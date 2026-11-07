package com.wael.chabchoub.portfoliomanager.security.ratelimit;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Rate-limits the public login and contact endpoints, per client IP and globally. */
public class RateLimitFilter extends OncePerRequestFilter {

	private record Rule(String method, String path, RateLimiter perIp, RateLimiter global) {
	}

	private final List<Rule> rules = List.of(
			// brute-force protection: 5 attempts per 15 minutes per IP
			new Rule("POST", "/auth/login", new RateLimiter(5, Duration.ofMinutes(15)),
					new RateLimiter(100, Duration.ofMinutes(15))),
			// spam protection: 5 messages per hour per IP
			new Rule("POST", "/api/emails/send", new RateLimiter(5, Duration.ofHours(1)),
					new RateLimiter(50, Duration.ofHours(1))));

	private final int trustedProxies;

	/**
	 * @param trustedProxies number of reverse proxies in front of the app that append to X-Forwarded-For
	 *                       (0 = ignore the header and use the socket address)
	 */
	public RateLimitFilter(int trustedProxies) {
		this.trustedProxies = trustedProxies;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		Rule rule = rules.stream()
				.filter(r -> r.method().equalsIgnoreCase(request.getMethod()) && r.path().equals(request.getRequestURI()))
				.findFirst().orElse(null);
		if (rule == null) {
			chain.doFilter(request, response);
			return;
		}

		long retryAfter = rule.perIp().tryAcquire(clientIp(request));
		if (retryAfter == 0) {
			retryAfter = rule.global().tryAcquire("*");
		}
		if (retryAfter > 0) {
			response.setStatus(429);
			response.setHeader("Retry-After", String.valueOf(retryAfter));
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write("{\"message\":\"TOO_MANY_REQUESTS\"}");
			return;
		}
		chain.doFilter(request, response);
	}

	String clientIp(HttpServletRequest request) {
		String forwarded = request.getHeader("X-Forwarded-For");
		if (trustedProxies > 0 && forwarded != null && !forwarded.isBlank()) {
			String[] parts = forwarded.split(",");
			int index = parts.length - trustedProxies;
			if (index >= 0) {
				return parts[index].trim();
			}
		}
		return request.getRemoteAddr();
	}
}
