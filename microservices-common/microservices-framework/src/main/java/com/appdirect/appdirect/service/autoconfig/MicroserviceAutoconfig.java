package com.appdirect.appdirect.service.autoconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.sleuth.TraceKeys;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.appdirect.appdirect.service.context.AppDirectRequestContextFilter;
import com.appdirect.appdirect.service.context.HystrixRequestContextFilter;
import com.appdirect.appdirect.service.context.RequestContextRequestInterceptor;
import com.appdirect.appdirect.service.context.RequestContextSetupListener;
import com.appdirect.appdirect.service.hystrix.HystrixLoggingExecutionHook;
import com.appdirect.appdirect.service.tracing.SpanPreservingConcurrencyStrategy;
import feign.RequestInterceptor;

/**
 * Auto-configuration bean for microservices. Sets up common framework beans related to context, tracing, etc.
 *
 * @author Dan Nathanson
 */
@Configuration
//@ConditionalOnMissingBean(annotation = Monolith.class)
public class MicroserviceAutoconfig {
	/**
	 * Add a filter to ensure that HystrixRequestContext is available in subthreads that make calls to external
	 * resources.
	 */
	@Bean
	public FilterRegistrationBean hystrixRequestContextFilterRegistration() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new HystrixRequestContextFilter());
		registrationBean.setName("hystrixContextFilter");
		registrationBean.setOrder(1);
		return registrationBean;
	}

	/**
	 * Add a filter to ensure that our AppDirectRequestContext object is added to HystrixRequestContext and
	 * propagated across service calls.
	 */
	@Bean
	@ConditionalOnProperty(value = "request.context.enabled", matchIfMissing = true)
	public FilterRegistrationBean requestContextFilterRegistration() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new AppDirectRequestContextFilter());
		registrationBean.setName("appDirectContextFilter");
		registrationBean.setOrder(2);
		return registrationBean;
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

	@Bean
	public HystrixLoggingExecutionHook getHystrixCommandExecutionHook() {
		return new HystrixLoggingExecutionHook();
	}

	@Bean
	SpanPreservingConcurrencyStrategy spanPreservingConcurrencyStrategy(Tracer tracer, TraceKeys traceKeys) {
		return new SpanPreservingConcurrencyStrategy(tracer, traceKeys);
	}

	@LoadBalanced
	@Bean
	RestTemplate loadBalancedRestTemplate() {
		return new RestTemplate();
	}
}
