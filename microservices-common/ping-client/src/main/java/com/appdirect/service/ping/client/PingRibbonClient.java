package com.appdirect.service.ping.client;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.client.RestTemplate;


/**
 * Example REST client for Ping Service using Spring REST Template with Netflix Ribbon
 * <p>
 * All calls are synchronous.  Is up to the user of the library to implement asynchronous execution if desired.
 *
 * @author Dan Nathanson.
 */
@Slf4j
public class PingRibbonClient implements PingClient {
	@LoadBalanced
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public String sendPingRequest(String message) {
		String results = restTemplate.getForObject("http://ping-service/ping/" + message, String.class);
		return results;
	}

	@Override
	public String sendGnipRequest(String message) {
		String results = restTemplate.getForObject("http://ping-service/gnip/" + message, String.class);
		return results;
	}

	@Override
	public String sendBothRequest(String message) {
		String results = restTemplate.getForObject("http://ping-service/both/" + message, String.class);
		return results;
	}

	public String sendOutOfOrderRequest(int seconds) {
		String results = restTemplate.getForObject("http://ping-service/ooo/" + seconds, String.class);
		return results;
	}


}
