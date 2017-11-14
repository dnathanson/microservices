package com.appdirect.appdirect.service.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import com.appdirect.appdirect.service.context.RequestContextRequestInterceptor;
import com.appdirect.appdirect.service.context.RequestContextSetupListener;
import com.appdirect.appdirect.service.gateway.filter.AppDirectRequestContextInitializationFilter;
import com.appdirect.service.ping.client.PingRibbonClient;
import com.appdirect.service.pong.client.PongRibbonClient;
import feign.RequestInterceptor;
import jersey.repackaged.com.google.common.collect.Lists;

/**
 * Simple service to test out Spring-Netflix OSS framework.
 *
 * @author Dan Nathanson.
 */
@EnableFeignClients
@EnableCircuitBreaker
@EnableRetry(proxyTargetClass = true)
@Component
@Configuration
public class ApiGateway {

	@Bean
	public AlwaysSampler defaultSampler() {
		return new AlwaysSampler();
	}

	@Bean
	PingRibbonClient pingRibbonClient() {
		return new PingRibbonClient();
	}

	@Bean
	PongRibbonClient pongRibbonClient() {
		return new PongRibbonClient();
	}

	/**
	 * Interceptor that automatically adds AppDirectRequestContext to all outgoing HTTP requests which use a
	 * Feign client
	 */
	@Bean
	RequestInterceptor requestContextRequestInterceptor() {
		return new RequestContextRequestInterceptor();
	}

	/**
	 * Container listener that tweaks RestTemplate so that Ribbon/RestTemplate-based clients get AppDirectRequestContext
	 * added to all outgoing HTTP requests.
	 */
	@Bean
	RequestContextSetupListener requestContextSetupListener() {
		return new RequestContextSetupListener();
	}

	/**
	 * Add a filter to ensure that our AppDirectRequestContext object is added to HystrixRequestContext and
	 * propagated across service calls.
	 */
	@Bean
	public FilterRegistrationBean requestContextInitializationFilterRegistration() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new AppDirectRequestContextInitializationFilter());
		registrationBean.setUrlPatterns(Lists.newArrayList("/api/*"));
		registrationBean.setName("requestContextInitializationFilter");
		registrationBean.setOrder(2);
		return registrationBean;
	}

	@Bean
	public RetryTemplate retryTemplate() {
		RetryTemplate retryTemplate = new RetryTemplate();
		FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
		fixedBackOffPolicy.setBackOffPeriod(2000L);
		retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(3);

		retryTemplate.setRetryPolicy(retryPolicy);
		return retryTemplate;
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiGateway.class, args);
	}
}
