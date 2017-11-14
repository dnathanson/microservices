package com.appdirect.service.monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.EnableTurbine;


/**
 * Spring Boot application runs both Hysterix Dashboard and Turbine applications
 *
 * @author Dan Nathanson.
 */
@EnableTurbine
@EnableDiscoveryClient
@SpringBootApplication()
public class TurbineServer {
	public static void main(String[] args) {
		SpringApplication.run(TurbineServer.class, args);
	}
}
