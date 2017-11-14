package com.appdirect.appdirect.service.context;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;

/**
 * Hystrix context variable for AppDirectRequestContext.  Hystrix will ensure that this variable is made available
 * in Hystrix context in any child threads used to send requests.
 *
 * @author Dan Nathanson
 */
public class AppDirectRequestContextHystrixContextVariable {
	private static final HystrixRequestVariableDefault<AppDirectRequestContext> appDirectRequestContextVariable =
			new HystrixRequestVariableDefault<>();

	private AppDirectRequestContextHystrixContextVariable() {
	}

	public static HystrixRequestVariableDefault<AppDirectRequestContext> getInstance() {
		return appDirectRequestContextVariable;
	}
}
