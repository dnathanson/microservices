package com.appdirect.service.pong;

import java.util.concurrent.atomic.AtomicLong;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appdirect.service.pong.model.PongResponse;

/**
 * Simple service to test out Spring-Netflix OSS framework.
 *
 * @author Dan Nathanson.
 */
@RestController
@Slf4j
@EnableConfigurationProperties
@Configuration
public class PongService {
	private AtomicLong serialNumber = new AtomicLong(0);

	@Autowired
	PongServiceSettings pongServiceSettings;

	/**
	 * Settings for service.  @RefreshScope allows settings to updated dynamically at runtime.
	 */
	@Bean
	@RefreshScope
	public PongServiceSettings pongServiceSettings() {
		return new PongServiceSettings();
	}

	@RequestMapping("/pong/{message}")
	public PongResponse pong(@PathVariable("message") String message) throws InterruptedException {
		checkOOO();
		Thread.sleep(100);
		log.debug("Received pong request: " + message);

		PongResponse response = new PongResponse();
		response.setRequestId(serialNumber.incrementAndGet());
		response.setMessage(pongServiceSettings.getResponseMessagePrefix() + message);
		return response;
	}

	@RequestMapping("/gnop/{message}")
	public String gnop(@PathVariable("message") String message) throws InterruptedException {
		checkOOO();
		Thread.sleep(200);
		log.debug("Received gnop request: " + message);
		return "Gnop: " + message;
	}

	@RequestMapping("/ooo/{flag}")
	public String ooo(@PathVariable("flag") boolean flag) {
		pongServiceSettings.setOutOfOrder(flag);
		if (flag) {
			log.info("Going down");
			return "Going out of order";
		} else {
			log.info("Coming back up");
			return "Back in service";
		}
	}

	@Bean
	PongHealthIndicator pongHealthIndicator() {
		PongHealthIndicator handler = new PongHealthIndicator();
		return handler;
	}

	@Bean
	public AlwaysSampler defaultSampler() {
		return new AlwaysSampler();
	}

	private void checkOOO() {
		if (pongServiceSettings.isOutOfOrder()) {
			log.error("Oh no! I'm out of order!");
			throw new IllegalStateException("Out Of Order");
		}
	}
}
