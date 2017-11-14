package com.appdirect.service.admin;

import java.util.List;

import org.springframework.cloud.client.discovery.DiscoveryClient;

import de.codecentric.boot.admin.discovery.ApplicationDiscoveryListener;
import de.codecentric.boot.admin.registry.ApplicationRegistry;

/**
 * Overridden ApplicationDiscoveryListener to ignore some services that do no support the standard /health URL for
 * health check.
 * <p>
 * Extend Admin Server's normal ApplicationDiscoveryListener so that it ignores itself and ignores Consul agent
 * (which has self registered but doesn't answer to standard /health URL.  Admin Server normally responds to /health,
 * but currently Admin Server has /admin-server web context configured to support proxying through Zuul so it's health
 * URL is /admin-server/health, but code in Spring Admin Server doesn't recognize the non-standard URL and keeps
 * calling /health.
 *
 * @author Dan Nathanson
 */
public class IgnorableServiceApplicationDiscoveryListener extends ApplicationDiscoveryListener {

	private final DiscoveryClient discoveryClient;
	private final ApplicationRegistry registry;
	private final List<String> ignoredServiceIds;


	public IgnorableServiceApplicationDiscoveryListener(DiscoveryClient discoveryClient, ApplicationRegistry registry, List<String> ignoredServiceIds) {
		super(discoveryClient, registry);
		this.registry = registry;
		this.discoveryClient = discoveryClient;
		this.ignoredServiceIds = ignoredServiceIds;
	}
    /*

	    */
/**
	 * For each service, if not the admin-server or consul, register all instances of the service.
	 */    /*

	@Override
	public void discover() {
		discoveryClient.getServices().stream()
				.filter(serviceId -> !ignoredServiceIds.contains(serviceId))
				.forEach(serviceId -> discoveryClient.getInstances(serviceId)
						.forEach(serviceInstance -> registry.register(convert(serviceInstance))));
	}
    */

}
