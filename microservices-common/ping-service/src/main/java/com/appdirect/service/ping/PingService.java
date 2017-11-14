package com.appdirect.service.ping;

import lombok.extern.slf4j.Slf4j;

import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Component;

import com.appdirect.service.pong.client.PongRibbonClient;

/**
 * Simple service to test out Spring-Netflix OSS framework.
 *
 * @author Dan Nathanson.
 */
@Configuration
@EnableFeignClients
@EnableCircuitBreaker
@EnableRetry(proxyTargetClass = true)
@Component
@Slf4j
public class PingService {
	@Bean
	public AlwaysSampler defaultSampler() {
		return new AlwaysSampler();
	}

	@Bean
	PongRibbonClient getPongRibbonClient() {
		return new PongRibbonClient();
	}
}
