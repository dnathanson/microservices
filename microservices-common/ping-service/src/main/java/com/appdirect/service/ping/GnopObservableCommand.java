package com.appdirect.service.ping;

import lombok.extern.slf4j.Slf4j;

import com.appdirect.service.pong.client.PongClient;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rx.Subscriber;

/**
 * Example of Hystrix command using native Netflix API with Observables
 *
 * @author Dan Nathanson.
 */
@Slf4j
public class GnopObservableCommand extends HystrixObservableCommand<String> {

	private PongClient pongClient;

	private String message;

	public GnopObservableCommand(PongClient pongClient, String message) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("PongService"))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)));

		this.pongClient = pongClient;
		this.message = message;
	}

	@Override
	protected Observable<String> construct() {
		return Observable.create(new Observable.OnSubscribe<String>() {
			@Override
			public void call(Subscriber<? super String> observer) {
				try {
					if (!observer.isUnsubscribed()) {
						log.debug("Sending gnop request: " + message);
						observer.onNext(pongClient.sendGnopRequest(message));
						observer.onCompleted();
					}
				} catch (Exception e) {
					observer.onError(e);
				}
			}
		});
	}

	@Override
	protected Observable<String> resumeWithFallback() {
		return Observable.just("Fallback gnop: " + message);
	}
}
