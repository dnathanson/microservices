package com.appdirect.service.ping;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Custom health check picked up by Spring Actuator and made available automatically via /health endpoint.
 *
 * @author Dan Nathanson.
 */
@Component
public class PingHealthIndicator implements HealthIndicator {
	@Override
	public Health health() {
		return new Health.Builder().up().withDetail("feeling", "happy!").build();
	}
}
