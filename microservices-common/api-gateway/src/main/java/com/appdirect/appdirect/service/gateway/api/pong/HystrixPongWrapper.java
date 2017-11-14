package com.appdirect.appdirect.service.gateway.api.pong;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appdirect.service.pong.model.PongResponse;
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
@Service("HystrixPongClient")
@Slf4j
public class HystrixPongWrapper {

	@Autowired
	private RetryPongWrapper retryPongWrapper;

	@HystrixCommand(fallbackMethod = "pongFallback",
			commandProperties = {@HystrixProperty(name = "execution.isolation.strategy", value = "THREAD")})
	public Observable<PongResponse> sendPongRequest(String message) {
		log.debug("Sending Pong request");
		return Observable.create(new Observable.OnSubscribe<PongResponse>() {
			@Override
			public void call(Subscriber<? super PongResponse> observer) {
				try {
					if (!observer.isUnsubscribed()) {
						observer.onNext(retryPongWrapper.sendPongRequest(message));
						observer.onCompleted();
					}
				} catch (Exception e) {
					observer.onError(e);
				}
			}
		});
	}

	@HystrixCommand(fallbackMethod = "gnopFallback",
			commandProperties = {@HystrixProperty(name = "execution.isolation.strategy", value = "THREAD")})
	public Observable<String> sendGnopRequest(final String message) {
		log.debug("Sending Gnop request");
		return Observable.create(new Observable.OnSubscribe<String>() {
			@Override
			public void call(Subscriber<? super String> observer) {
				try {
					if (!observer.isUnsubscribed()) {
						observer.onNext(retryPongWrapper.sendGnopRequest(message));
						observer.onCompleted();
					}
				} catch (Exception e) {
					observer.onError(e);
				}
			}
		});
	}

	public Observable<PongResponse> pongFallback(String message) {
		log.warn("Falling back for Pong request");
		return Observable.just(new PongResponse(-1, "Fallback pong: " + message));
	}

	public Observable<String> gnopFallback(String message) {
		log.warn("Falling back for Gnop request");
		return Observable.just("Fallback gnop: " + message);
	}

}
