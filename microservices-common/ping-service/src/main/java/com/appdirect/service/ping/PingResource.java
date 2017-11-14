package com.appdirect.service.ping;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appdirect.service.pong.model.PongResponse;

/**
 * REST endpoint for GET /ping using Spring REST support.  Uses Feign client to actually make the REST call.
 *
 * @author Dan Nathanson.
 */
@RestController
@Slf4j
public class PingResource {

	@Autowired
	private PongFeignClient feignClient;

	@RequestMapping("/ping/{message}")
	public String ping(@PathVariable("message") String message) {
		log.debug("Received ping request: " + message);
		PongResponse pongResponse = feignClient.sendPongRequest(message);
		log.debug("Done with ping request: " + message);
		return "Ping: " + pongResponse.getMessage();
	}
}
