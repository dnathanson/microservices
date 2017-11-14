package com.appdirect.service.pong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

/**
 * Simple service to test out Spring-Netflix OSS framework.
 *
 * @author Dan Nathanson.
 */
@SpringBootApplication
@EnableEurekaClient
public class Application {

	@Bean
	PongHealthCheckHandler healthCheckHandler() {
		PongHealthCheckHandler handler = new PongHealthCheckHandler();
		return handler;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
