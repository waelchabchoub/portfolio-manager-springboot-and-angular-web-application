package com.wael.chabchoub.portfoliomanager.security.ratelimit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class RateLimitFilterTest {

	@Test
	void limiterBlocksAfterMaxAndReleasesAfterWindow() {
		AtomicLong now = new AtomicLong(1_000);
		RateLimiter limiter = new RateLimiter(2, Duration.ofSeconds(10), now::get);

		assertEquals(0, limiter.tryAcquire("a"));
		assertEquals(0, limiter.tryAcquire("a"));
		assertTrue(limiter.tryAcquire("a") > 0);
		assertEquals(0, limiter.tryAcquire("b"), "other keys are independent");

		now.addAndGet(10_000);
		assertEquals(0, limiter.tryAcquire("a"), "window has passed");
	}

	@Test
	void loginIsLimitedPerIp() throws Exception {
		RateLimitFilter filter = new RateLimitFilter(0);

		for (int i = 0; i < 5; i++) {
			assertEquals(200, call(filter, "POST", "/auth/login", "10.0.0.1", null).getStatus());
		}
		MockHttpServletResponse blocked = call(filter, "POST", "/auth/login", "10.0.0.1", null);
		assertEquals(429, blocked.getStatus());
		assertTrue(Long.parseLong(blocked.getHeader("Retry-After")) > 0);

		assertEquals(200, call(filter, "POST", "/auth/login", "10.0.0.2", null).getStatus(), "other IP is fine");
	}

	@Test
	void otherPathsAreNotLimited() throws Exception {
		RateLimitFilter filter = new RateLimitFilter(0);
		for (int i = 0; i < 50; i++) {
			assertEquals(200, call(filter, "GET", "/api/skill/get", "10.0.0.1", null).getStatus());
		}
	}

	@Test
	void forwardedHeaderIsUsedOnlyWhenAProxyIsTrusted() throws Exception {
		RateLimitFilter trusting = new RateLimitFilter(1);
		// the client rotates a spoofed first entry; the last entry is the one the proxy added
		for (int i = 0; i < 5; i++) {
			call(trusting, "POST", "/auth/login", "10.0.0.9", "spoof-" + i + ", 203.0.113.7");
		}
		assertEquals(429, call(trusting, "POST", "/auth/login", "10.0.0.9", "another, 203.0.113.7").getStatus());

		RateLimitFilter ignoring = new RateLimitFilter(0);
		for (int i = 0; i < 5; i++) {
			call(ignoring, "POST", "/auth/login", "10.0.0.9", "spoof-" + i);
		}
		assertEquals(429, call(ignoring, "POST", "/auth/login", "10.0.0.9", "new-spoof").getStatus());
	}

	private MockHttpServletResponse call(RateLimitFilter filter, String method, String uri, String remoteAddr,
			String forwardedFor) throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest(method, uri);
		request.setRemoteAddr(remoteAddr);
		if (forwardedFor != null) {
			request.addHeader("X-Forwarded-For", forwardedFor);
		}
		MockHttpServletResponse response = new MockHttpServletResponse();
		filter.doFilter(request, response, new MockFilterChain());
		return response;
	}
}
