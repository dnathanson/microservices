package com.appdirect.service.ping.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Client API for Ping Service. A Netflix Ribbon implementation is provided.  This interface can also be used
 * to declare a Netflix Feign client implementation by extending this interface and adding the @FeignClient
 * annotation on the new interface.
 * <p>
 * TODO: Figure out a way to add AppDirectRequestContext (as JSON) to header when using Feign client.  Maybe
 * by using custom Feign RequestInterceptor, but would require constructing Feign clients in code, rather than
 * with a simple Annotation. Something to explore.
 *
 * @author Dan Nathanson.
 */
public interface PingClient {

	@RequestMapping(method = RequestMethod.GET, value = "/gnop/{message}")
	@ResponseBody
	String sendGnipRequest(@PathVariable("message") String message);

	@RequestMapping(method = RequestMethod.GET, value = "/pong/{message}")
	@ResponseBody
	String sendPingRequest(@PathVariable("message") String message);

	@RequestMapping(method = RequestMethod.GET, value = "/both/{message}")
	@ResponseBody
	String sendBothRequest(@PathVariable("message") String message);
}
