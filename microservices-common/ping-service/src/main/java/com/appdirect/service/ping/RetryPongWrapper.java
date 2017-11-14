package com.appdirect.service.ping;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.appdirect.service.pong.client.PongClient;
import com.appdirect.service.pong.client.PongRibbonClient;
import com.appdirect.service.pong.model.PongResponse;

/**
 * Retry wrapper for calls to Pong Service endpoints.   This class is necessary (at this time) because the order
 * that @HystrixCommand and @Retryable annotations aspects get woven in is non-deterministic when both are present
 * on a single method.
 *
 * @author Dan Nathanson.
 */
@Component
@Slf4j
public class RetryPongWrapper implements PongClient {
	@Autowired
	private PongRibbonClient ribbonClient;


	@Override
	@Retryable
	public PongResponse sendPongRequest(String message) {
		log.debug("Sending Pong request");
		PongResponse pongResponse = ribbonClient.sendPongRequest(message);
		log.debug("Received Pong response");
		return pongResponse;
	}

	@Override
	@Retryable
	public String sendGnopRequest(final String message) {
		log.debug("Sending Gnop request");
		String gnopResponse = ribbonClient.sendGnopRequest(message);
		log.debug("Received Gnop response");
		return gnopResponse;
	}
}
