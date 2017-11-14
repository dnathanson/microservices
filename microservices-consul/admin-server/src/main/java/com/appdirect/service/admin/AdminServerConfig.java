package com.appdirect.service.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration for Admin Server
 * @author Dan Nathanson
 */
@Component
@ConfigurationProperties(prefix = "admin.server")
public class AdminServerConfig {
	private List<String> ignoredServiceIds = new ArrayList<>();

	public List<String> getIgnoredServiceIds() {
		return ignoredServiceIds;
	}
}
