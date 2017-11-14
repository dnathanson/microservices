package com.appdirect.appdirect.service.hystrix;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;

/**
 * Hystrix execution hook that logs exception. Needed because Hystrix can swallow the exception when it has a fallback
 * method that it can call.
 *
 * @author Dan Nathanson
 */
@Slf4j
public class HystrixLoggingExecutionHook extends HystrixCommandExecutionHook {

	@Override
	public <T> Exception onExecutionError(HystrixInvokable<T> commandInstance, Exception e) {
		log.error("Exception during execution of Hystrix command.", e);
		return super.onExecutionError(commandInstance, e);
	}

	@PostConstruct
	public void registerExecutionHook() {
		HystrixPlugins.getInstance().registerCommandExecutionHook(this);
	}

}
