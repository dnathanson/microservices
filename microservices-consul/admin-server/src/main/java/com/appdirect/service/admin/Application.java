package com.appdirect.service.admin;

import de.codecentric.boot.admin.registry.ApplicationRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Simple Spring Boot application that runs CodeCentric's Spring Boot Admin Server.
 *
 * @author Dan Nathanson.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class Application {

	@Value("${spring.boot.admin.discovery.management.context-path:}")
	private String managementPath;

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private ApplicationRegistry registry;

	@Autowired
	private AdminServerConfig adminServerConfig;

    /*
	    */
/**
	 * Override ApplicationDiscoveryListener to ignore some services that do no support the standard /health URL for
	 * health check.
	 */    /*

	@Bean
	ApplicationDiscoveryListener applicationDiscoveryListener() {
		ApplicationDiscoveryListener listener = new IgnorableServiceApplicationDiscoveryListener(discoveryClient, registry, adminServerConfig.getIgnoredServiceIds());
		listener.setManagementContextPath(managementPath);
		return listener;
	}
    */

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
