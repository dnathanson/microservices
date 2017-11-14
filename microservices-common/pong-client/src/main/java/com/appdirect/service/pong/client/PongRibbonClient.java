package com.appdirect.service.pong.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.appdirect.service.pong.model.PongResponse;

/**
 * Example REST client for Pong Service using Netflix Ribbon with service discovery and client-side load
 * balancing.
 * <p>
 * All calls are synchronous.  Is up to the user of the library to implement asynchronous execution if desired.
 * <p>
 * Spring-based applications can use this client simply by declaring a bean with this type when Spring Web,
 * Spring Cloud and Spring Cloud Netflix are included as dependencies.
 *
 * @author Dan Nathanson.
 */
public class PongRibbonClient implements PongClient {
	@Autowired
	@LoadBalanced
	private RestTemplate restTemplate;

	@Override
	public String sendGnopRequest(String message) {
		String results = restTemplate.getForObject("http://pong-service/gnop/" + message, String.class);
		return results;
	}

	@Override
	public PongResponse sendPongRequest(String message) {
		ResponseEntity<PongResponse> results =
				restTemplate.getForEntity("http://pong-service/pong/" + message, PongResponse.class);
		return results.getBody();
	}

	public String sendOutOfOrderRequest(int seconds) {
		String results = restTemplate.getForObject("http://pong-service/ooo/" + seconds, String.class);
		return results;
	}
}
