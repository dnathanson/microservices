package com.appdirect.appdirect.service.gateway.api.pingpong;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.appdirect.appdirect.service.gateway.api.ping.HystrixPingWrapper;
import com.appdirect.appdirect.service.gateway.api.pong.HystrixPongWrapper;
import com.appdirect.service.pong.model.PongResponse;
import jersey.repackaged.com.google.common.collect.ImmutableMap;
import rx.Observable;

/**
 * REST controller for "pingping" endpoint.  Calls both the "ping" and "pong" endpoints on Ping Service and Pong
 * Service, respectively, asynchronously (because they are using the Hystrix wrapper which by default executes in a
 * different thread). Uses RxJava to wait on threads to complete on another thread (while request processing thread
 * is released back to Tomcat).
 *
 * @author Dan Nathanson.
 */
@RestController
@Slf4j
public class PingPongApi {

	@Autowired
	private HystrixPingWrapper pingWrapper;
	@Autowired
	private HystrixPongWrapper pongWrapper;

	/**
	 * Calls 'ping' endpoint on Ping Service and 'pong' endpoint on Pong Service.  Response is JSON structure:
	 * <pre>
	 * {
	 *   "ping": "Ping: Pong: foo",
	 *   "pong": "Pong: foo"
	 * }
	 * </pre>
	 */
	@RequestMapping("/api/pingpong/{message}")
	public DeferredResult<Map<String, String>> both(@PathVariable("message") String message) {

		// This thread is a Tomcat HTTP request processing thread
		log.info("Handle pingpong request");
		final DeferredResult<Map<String, String>> deferredResult = new DeferredResult<>();

		// These calls are each run in their own threads by Hystrix
		Observable<PongResponse> pongObservable = pongWrapper.sendPongRequest(message);
		Observable<String> pingObservable = pingWrapper.sendPingRequest(message);

		// With whichever Hystrix thread returns last, combine responses from pong and ping requests into Map
		Observable
				.zip(pongObservable, pingObservable, (PongResponse pongResponse, String pingMessage) ->
						ImmutableMap.<String, String>builder()
								.put("ping", pingMessage)
								.put("pong", pongResponse.getMessage())
								.build())
				.subscribe(messages -> {
					// This thread is from the ThreadPoolExecutor
					log.info("Combining ping + pong responses and setting result");

					deferredResult.setResult(messages);
				});

		// This thread is the Tomcat HTTP request processing thread
		log.info("Finished pingpong request async");
		return deferredResult;
	}
}
