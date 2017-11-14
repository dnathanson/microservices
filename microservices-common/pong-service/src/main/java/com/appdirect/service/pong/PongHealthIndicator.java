package com.appdirect.service.pong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Custom health check picked up by Spring Actuator and made available automatically via /health endpoint.
 *
 * @author Dan Nathanson.
 */
@Component
public class PongHealthIndicator implements HealthIndicator {
	@Autowired
	PongServiceSettings serviceSettings;

	@Override
	public Health health() {
		if (serviceSettings.isOutOfOrder()) {
			return new Health.Builder().down().withDetail("OutOfOrder", "TRUE").build();
		} else {
			return new Health.Builder().up().withDetail("feeling", "happy!").build();
		}
	}
}
