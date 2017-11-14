package com.appdirect.service.monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;


/**
 * Spring Boot application which runs Hystrix Dashboard
 *
 * @author Dan Nathanson.
 */
@EnableHystrixDashboard
@EnableDiscoveryClient
@SpringBootApplication()
public class HystrixDashboardServer {
	public static void main(String[] args) {
		SpringApplication.run(HystrixDashboardServer.class, args);
	}
}
