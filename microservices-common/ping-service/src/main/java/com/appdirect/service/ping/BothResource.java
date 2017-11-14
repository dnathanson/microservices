package com.appdirect.service.ping;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.appdirect.service.pong.model.PongResponse;
import rx.Observable;

/**
 * REST controller for "both" endpoint.  Calls both the "pong" and "gnop" endpoints on Pong Service asynchronously
 * (because they are using the Hystrix wrapper which by default executes in a different thread). Uses RxJava
 * to wait on threads to complete on another thread (while request processing thread is releases back to Tomcat).
 *
 * @author Dan Nathanson.
 */
@RestController
@Slf4j
public class BothResource {

	@Autowired
	private HystrixPongWrapper pongWrapper;

	@RequestMapping("/both/{message}")
	public DeferredResult<String> both(@PathVariable("message") String message) {

		// This thread is a Tomcat HTTP request processing thread
		log.info("Handle both request");
		final DeferredResult<String> deferredResult = new DeferredResult<>();

		// These calls are each run in their own threads by Hystrix
		Observable<PongResponse> pongResponse = pongWrapper.sendPongRequest(message);
		Observable<String> gnopResponse = pongWrapper.sendGnopRequest(message);

		// With whichever Hystrix thread returns last, combine responses from pong and gnop requests (pong + gnop)
		Observable
				.zip(pongResponse, gnopResponse, (PongResponse pongMessage, String gnopMessage) -> pongMessage.getMessage() + " + " + gnopMessage)
				.subscribe(resp -> {
					log.info("Sending both response");
					deferredResult.setResult("Both: " + resp);
				});

		// This thread is the Tomcat HTTP request processing thread
		log.info("Finished both request async");
		return deferredResult;
	}
}
