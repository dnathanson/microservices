package com.appdirect.appdirect.service.gateway.api.ping;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import rx.Observable;
import rx.Subscriber;

/**
 * Hystrix wrapper for calls to Pong Service endpoints. By default, methods annotated with HystrixCommand will
 * execute in a new thread.
 *
 * @author Dan Nathanson.
 */
@Component
@Slf4j
public class HystrixPingWrapper {
	@Autowired
	private RetryPingWrapper retryPingWrapper;

	@HystrixCommand(fallbackMethod = "pingFallback",
			commandProperties = {@HystrixProperty(name = "execution.isolation.strategy", value = "THREAD")})
	public Observable<String> sendPingRequest(String message) {
		log.debug("Sending Ping request");
		return Observable.create(new Observable.OnSubscribe<String>() {
			@Override
			public void call(Subscriber<? super String> observer) {
				try {
					if (!observer.isUnsubscribed()) {
						observer.onNext(retryPingWrapper.sendPingRequest(message));
						observer.onCompleted();
					}
				} catch (Exception e) {
					observer.onError(e);
				}
			}
		});
	}

	@HystrixCommand(fallbackMethod = "gnipFallback",
			commandProperties = {@HystrixProperty(name = "execution.isolation.strategy", value = "THREAD")})
	public Observable<String> sendGnopRequest(final String message) {
		log.debug("Sending Gnop request");
		return Observable.create(new Observable.OnSubscribe<String>() {
			@Override
			public void call(Subscriber<? super String> observer) {
				try {
					if (!observer.isUnsubscribed()) {
						observer.onNext(retryPingWrapper.sendGnipRequest(message));
						observer.onCompleted();
					}
				} catch (Exception e) {
					observer.onError(e);
				}
			}
		});
	}

	@HystrixCommand(fallbackMethod = "bothFallback",
			commandProperties = {@HystrixProperty(name = "execution.isolation.strategy", value = "THREAD")})
	public Observable<String> sendBothRequest(@PathVariable("message") String message) {
		log.debug("Sending Both request");
		return Observable.create(new Observable.OnSubscribe<String>() {
			@Override
			public void call(Subscriber<? super String> observer) {
				try {
					if (!observer.isUnsubscribed()) {
						observer.onNext(retryPingWrapper.sendBothRequest(message));
						observer.onCompleted();
					}
				} catch (Exception e) {
					observer.onError(e);
				}
			}
		});
	}

	public Observable<String> pingFallback(String message) {
		log.warn("Falling back for Ping request");
		return Observable.just("Fallback ping: " + message);
	}

	public Observable<String> gnipFallback(String message) {
		log.warn("Falling back for Gnip request");
		return Observable.just("Fallback gnip: " + message);
	}

	public Observable<String> bothFallback(String message) {
		log.warn("Falling back for Both request");
		return Observable.just("Fallback both: " + message);
	}
}
