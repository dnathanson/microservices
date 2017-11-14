package com.appdirect.service.ping;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appdirect.service.pong.model.PongResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import rx.Observable;
import rx.Subscriber;

/**
 * Example using Spring Cloud's Hystrix integration with HystrixCommand annotations.  Hystrix commands, by default,
 * execute in a separate thread.
 *
 * @author Dan Nathanson.
 */
@Component
@Slf4j
public class HystrixPongWrapper {
	@Autowired
	private RetryPongWrapper retryPongWrapper;

	@HystrixCommand(fallbackMethod = "pongFallback",
			commandProperties = {@HystrixProperty(name = "execution.isolation.strategy", value = "THREAD")})
	public Observable<PongResponse> sendPongRequest(String message) {
		log.info("Sending Pong request with thread [" + Thread.currentThread().getName() + "]");
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
		log.info("Sending Gnop request with thread [" + Thread.currentThread().getName() + "]");
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

	/**
	 * Fallback for {@link #sendPongRequest(String)}.  Example of chained fallback strategy.  This method will
	 * fallback to {@link #pongFallback2(String)}.
	 */
	@HystrixCommand(fallbackMethod = "pongFallback2")
	public Observable<PongResponse> pongFallback(String message) {
		log.info("Falling back for Pong request with thread [" + Thread.currentThread().getName() + "]");
		throw new RuntimeException("Fallback failed, too!");
	}

	/**
	 * Fallback for {@link #pongFallback(String)}.
	 */
	public Observable<PongResponse> pongFallback2(String message) {
		log.info("Double falling back for Pong request with thread [" + Thread.currentThread().getName() + "]");
		return Observable.just(new PongResponse(0, "Fallback2 pong: " + message));
	}

	public Observable<String> gnopFallback(String message) {
		log.info("Falling back for Gnop request with thread [" + Thread.currentThread().getName() + "]");
		return Observable.just("Fallback gnop: " + message);
	}
}
