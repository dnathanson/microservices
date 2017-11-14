package com.appdirect.appdirect.service.gateway.api.ping;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API REST endpoints Ping Service.  Uses {@link HystrixPingWrapper} to call Ping Service's endpoint.
 *
 * @author Dan Nathanson.
 */
@RestController
@Slf4j
public class PingApi {
	@Autowired
	private HystrixPingWrapper pingWrapper;


	/**
	 * Calls 'ping' endpoint on Ping Service.  Ping Service in turn calls Pong Service's 'pong' endpoint. Response
	 * should be "API: Ping: Pong: {message}.
	 */
	@RequestMapping("/api/ping/{message}")
	public String pong(@PathVariable("message") String message) {
		String response = pingWrapper.sendPingRequest(message).toBlocking().single();
		return "API: " + response;
	}

	/**
	 * Calls 'both' endpoint on Ping Service.  Ping Service in turn calls both the 'pong' and the 'gnop' endpoints on
	 * the  Pong Service'. Response should be "API: Ping: Pong: {message}.
	 */
	@RequestMapping("/api/both/{message}")
	public String both(@PathVariable("message") String message) {
		String response = pingWrapper.sendBothRequest(message).toBlocking().single();
		return "API: " + response;
	}

	/**
	 * Calls 'gnip' endpoint on Ping Service.  Ping Service in turn calls Pong Service's 'gnop' endpoint. Response
	 * should be "API: Gnip: Gnop: {message}.
	 */
	@RequestMapping("/api/gnip/{message}")
	public String gnop(@PathVariable("message") String message) {
		String response = pingWrapper.sendGnopRequest(message).toBlocking().single();
		return "API: " + response;
	}

}
