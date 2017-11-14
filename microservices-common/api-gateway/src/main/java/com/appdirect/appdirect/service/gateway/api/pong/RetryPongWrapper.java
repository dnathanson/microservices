package com.appdirect.appdirect.service.gateway.api.pong;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.appdirect.service.pong.client.PongClient;
import com.appdirect.service.pong.client.PongRibbonClient;
import com.appdirect.service.pong.model.PongResponse;

/**
 * Retry wrapper for calls to Pong Service endpoints.
 * <p>
 * Ideally, the @Retryable annotation could be put on the same method as @HystrixCommand (see {@link HystrixPongWrapper})
 * but the order that the AOP interceptor for those two annotations is the same and it is indeterminate which
 * one gets executed first.  If the HystrixCommand interceptor runs first, the retry never happens because the
 * fallback is successful.
 *
 * @author Dan Nathanson.
 */
@Slf4j
@Component
public class RetryPongWrapper implements PongClient {
	@Autowired
	private PongRibbonClient ribbonClient;

	@Override
	@Retryable(maxAttempts = 2)
	public PongResponse sendPongRequest(String message) {
		log.info("Sending Pong request");
		PongResponse response = ribbonClient.sendPongRequest(message);
		log.info("Received Pong response");
		return response;
	}

	@Override
	@Retryable
	public String sendGnopRequest(final String message) {
		log.info("Sending Gnop request");
		String response = ribbonClient.sendGnopRequest(message);
		log.info("Received Gnop response");
		return response;
	}
}
