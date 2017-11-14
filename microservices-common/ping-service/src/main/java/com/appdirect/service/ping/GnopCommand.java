package com.appdirect.service.ping;

import com.appdirect.service.pong.client.PongClient;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * Example of Hystrix wrapped command using native Netflix API.
 *
 * @author Dan Nathanson.
 */
public class GnopCommand extends HystrixCommand<String> {

	private PongClient pongClient;

	private String message;

	public GnopCommand(PongClient pongClient, String message) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("PongService"))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)));

		this.pongClient = pongClient;
		this.message = message;
	}


	@Override
	protected String getFallback() {
		return "Fallback gnop: " + message;
	}

	@Override
	protected String run() throws Exception {
		return pongClient.sendGnopRequest(message);
	}
}
