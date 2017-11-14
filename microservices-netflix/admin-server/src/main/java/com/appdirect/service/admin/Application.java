package com.appdirect.service.admin;

import de.codecentric.boot.admin.registry.ApplicationRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Simple service to test out Spring-Netflix OSS framework.
 *
 * @author Dan Nathanson.
 */
@SpringBootApplication
@EnableEurekaClient
public class Application {
	@Value("${spring.boot.admin.discovery.management.context-path:}")
	private String managementPath;

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private ApplicationRegistry registry;


    /*
	@Bean
	ApplicationDiscoveryListener applicationDiscoveryListener() {
		EurekaApplicationDiscoveryListener listener = new EurekaApplicationDiscoveryListener(discoveryClient, registry);
		listener.setManagementContextPath(managementPath);
		return listener;
	}
    */

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
