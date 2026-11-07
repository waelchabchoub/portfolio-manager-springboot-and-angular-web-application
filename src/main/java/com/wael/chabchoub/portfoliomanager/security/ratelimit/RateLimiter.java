package com.wael.chabchoub.portfoliomanager.security.ratelimit;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.LongSupplier;

/** In-memory sliding-window limiter: at most {@code maxRequests} per key within the window. */
public class RateLimiter {
	private static final int CLEANUP_THRESHOLD = 10_000;

	private final int maxRequests;
	private final long windowMillis;
	private final LongSupplier clock;
	private final Map<String, Deque<Long>> hits = new ConcurrentHashMap<>();

	public RateLimiter(int maxRequests, Duration window) {
		this(maxRequests, window, System::currentTimeMillis);
	}

	RateLimiter(int maxRequests, Duration window, LongSupplier clock) {
		this.maxRequests = maxRequests;
		this.windowMillis = window.toMillis();
		this.clock = clock;
	}

	/** Returns 0 and records the request if allowed, otherwise the seconds to wait. */
	public long tryAcquire(String key) {
		long now = clock.getAsLong();
		if (hits.size() > CLEANUP_THRESHOLD) {
			evictExpired(now);
		}
		Deque<Long> times = hits.computeIfAbsent(key, k -> new ArrayDeque<>());
		synchronized (times) {
			while (!times.isEmpty() && now - times.peekFirst() >= windowMillis) {
				times.pollFirst();
			}
			if (times.size() >= maxRequests) {
				long waitMillis = windowMillis - (now - times.peekFirst());
				return Math.max(1, (waitMillis + 999) / 1000);
			}
			times.addLast(now);
			return 0;
		}
	}

	private void evictExpired(long now) {
		hits.entrySet().removeIf(entry -> {
			synchronized (entry.getValue()) {
				Long newest = entry.getValue().peekLast();
				return newest == null || now - newest >= windowMillis;
			}
		});
	}
}
