package com.appdirect.service.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Spring Boot application that runs Netflix Zuul Server as a proxy for API-Gateway calls.
 *
 * @author Dan Nathanson.
 */
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication()
public class ZuulServer {
	public static void main(String[] args) {
		SpringApplication.run(ZuulServer.class, args);
	}
}
