package com.appdirect.service.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Spring Boot application that runs Netflix Eureka Server to support service registration and discovery.
 *
 * @author Dan Nathanson.
 */
@EnableEurekaServer
@EnableEurekaClient
@SpringBootApplication()
public class EurekaServer {
	public static void main(String[] args) {
		SpringApplication.run(EurekaServer.class, args);
	}
}
