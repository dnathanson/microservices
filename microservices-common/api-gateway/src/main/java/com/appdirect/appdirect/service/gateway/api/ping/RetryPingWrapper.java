package com.appdirect.appdirect.service.gateway.api.ping;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.appdirect.service.ping.client.PingClient;
import com.appdirect.service.ping.client.PingRibbonClient;

/**
 * Retry wrapper for calls to Ping Service endpoints.   This class is necessary (at this time) because the order
 * that @HystrixCommand and @Retryable annotations aspects get woven in is non-deterministic when both are present
 * on a single method.
 *
 * @author Dan Nathanson.
 */
@Component
@Slf4j
public class RetryPingWrapper implements PingClient {
	@Autowired
	private PingRibbonClient ribbonClient;

	@Override
	@Retryable
	public String sendPingRequest(String message) {
		log.info("Sending Ping request");
		String response = ribbonClient.sendPingRequest(message);
		log.info("Received Ping response");
		return response;
	}

	@Override
	@Retryable
	public String sendBothRequest(@PathVariable("message") String message) {
		log.info("Sending Both request");
		String response = ribbonClient.sendBothRequest(message);
		log.info("Received Both response");
		return response;
	}

	@Override
	@Retryable
	public String sendGnipRequest(final String message) {
		log.info("Sending Gnip request");
		String response = ribbonClient.sendGnipRequest(message);
		log.info("Received Gnip response");
		return response;
	}
}
