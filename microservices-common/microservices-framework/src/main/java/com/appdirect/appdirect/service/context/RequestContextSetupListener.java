package com.appdirect.appdirect.service.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.client.RestTemplate;

/**
 * Event listener that replaces the RibbonClientHttpRequestFactory in the RestTemplate with one that automatically
 * adds the AppDirect Request Context as header variable in all HTTP request (to other services).
 * <p>
 * There is probably a way to get this done by overriding the standard RestTemplate bean, but I can't get it
 * to work.  Keeps clashing with RibbonAutoConfiguration which also wants to replace the ClientHttpRequestFactory.
 * After banging my head against this for a while, I decided to just let that class do it's thing during bean
 * initialization and then replace it after everything else is done.
 */
public class RequestContextSetupListener implements ApplicationListener<ContextRefreshedEvent> {
	@Autowired
	private SpringClientFactory springClientFactory;

    @Autowired
	private RestTemplate restTemplate;


	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		restTemplate.setRequestFactory(new RequestContextAddingClientHttpRequestFactory(springClientFactory));
	}
}
