package com.appdirect.service.pong.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.appdirect.service.pong.model.PongResponse;

/**
 * Client API for Pong Service. A Netflix Ribbon implementation is provided.  This interface can also be used
 * to declare a Netflix Feign client implementation by extending this interface and adding the @FeignClient
 * annotation on the new interface.
 *
 * @author Dan Nathanson.
 */
public interface PongClient {

	@RequestMapping(method = RequestMethod.GET, value = "/gnop/{message}")
	@ResponseBody
	String sendGnopRequest(@PathVariable("message") String message);

	@RequestMapping(method = RequestMethod.GET, value = "/pong/{message}")
	@ResponseBody
	PongResponse sendPongRequest(@PathVariable("message") String message);
}
