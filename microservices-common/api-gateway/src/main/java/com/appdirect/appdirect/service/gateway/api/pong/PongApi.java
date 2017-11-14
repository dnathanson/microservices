package com.appdirect.appdirect.service.gateway.api.pong;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appdirect.appdirect.service.gateway.api.ping.HystrixPingWrapper;
import com.appdirect.service.pong.model.PongResponse;

/**
 * API REST endpoints Pong Service.  Uses {@link HystrixPingWrapper} to call Pong Service's endpoint.
 *
 * @author Dan Nathanson.
 */
@RestController
@Slf4j
public class PongApi {
	@Autowired
	private HystrixPongWrapper pongWrapper;

	@Autowired
	PongFeignClient feignClient;

	/**
	 * Calls 'pong' endpoint on Pong Service. Response should be "API: Pong: {message}.
	 */
	@RequestMapping("/api/pong/{message}")
	public String pong(@PathVariable("message") String message) {
		log.info("Sending pong request using Feign client: " + message);
		PongResponse response = feignClient.sendPongRequest(message);
		return "API: " + response.getMessage();
	}

	/**
	 * Calls 'gnop' endpoint on Pong Service. Response should be "API: Gnop: {message}.
	 */
	@RequestMapping("/api/gnop/{message}")
	public String gnop(@PathVariable("message") String message) {
		String response = pongWrapper.sendGnopRequest(message).toBlocking().single();
		return "API: " + response;
	}

}
