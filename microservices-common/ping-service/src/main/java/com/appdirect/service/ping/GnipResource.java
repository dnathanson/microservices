package com.appdirect.service.ping;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import rx.Observable;

/**
 * REST controller for "gnip" endpoint. Calls Pong Service's "gnop" endpoint using HystrixObservableCommand.
 *
 * @author Dan Nathanson.
 */
@RestController
@Slf4j
public class GnipResource {

	@Autowired
	RetryPongWrapper retryClient;

	/**
	 * Handle GET /gnip/{message}. Rather than calling Pong Service directly, uses a subclass of
	 * HystrixObservableCommand to wrap call to external resource in a circuit breaker. Command is run in
	 * a separate thread from the HTTP request processing thread.
	 */
	@RequestMapping("/gnip/{message}")
	public DeferredResult<String> gnip(@PathVariable("message") String message) {
		log.info("Handle gnip request with thread [" + Thread.currentThread().getName() + "]");

		GnopObservableCommand gnopCommand = new GnopObservableCommand(retryClient, message);

		final DeferredResult<String> deferredResult = new DeferredResult<>();
		Observable<String> gnopResponse = gnopCommand.toObservable();

		// Request is not sent until the observable is subscribed to at which point the request is sent on thread from
		// the Hystrix thread pool.
		gnopResponse.subscribe(resp -> {
					log.info("Handle gnop response with thread [" + Thread.currentThread().getName() + "]");
					deferredResult.setResult("Gnip: " + resp);
				},
				e -> {
					log.error("Hmm.  Strange.  Why didn't we get the fallback?", e);
					deferredResult.setResult("Couldn't get gnop response. No fallback.");
				});
		log.info("Done handling gnip request with thread [" + Thread.currentThread().getName() + "]");

		return deferredResult;
	}
}
