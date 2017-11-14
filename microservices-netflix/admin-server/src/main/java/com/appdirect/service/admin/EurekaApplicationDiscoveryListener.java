package com.appdirect.service.admin;

import de.codecentric.boot.admin.discovery.ApplicationDiscoveryListener;
import de.codecentric.boot.admin.registry.ApplicationRegistry;
import org.springframework.cloud.client.discovery.DiscoveryClient;

/**
 * Implementation of ApplicationDiscoveryListener which is more tightly integrated to Eureka allowing health check
 * URL published by Eureka clients to be used rather than just building the health check URL for each application
 * blindly.   Added because we wanted to change the servlet context path for Admin Server to be /admin-server so
 * we can proxy calls to this service through Zuul and still have the UI work (relative path to static
 * resources have to start with /admin-server also).
 *
 * @author Dan Nathanson
 */
public class EurekaApplicationDiscoveryListener extends ApplicationDiscoveryListener {
	private final DiscoveryClient discoveryClient;
	private final ApplicationRegistry registry;
	private String managementContextPath;
	private String healthEndpoint;
	private String serviceContextPath;

	public EurekaApplicationDiscoveryListener(DiscoveryClient discoveryClient, ApplicationRegistry registry) {
		super(discoveryClient, registry);
		this.discoveryClient = discoveryClient;
		this.registry = registry;
	}
     /*

	@Override
	public void discover() {
		for (String serviceId : discoveryClient.getServices()) {
			for (ServiceInstance instance : discoveryClient.getInstances(serviceId)) {
				registry.register(convert(instance));
			}
		}
	}

	@Override
	public void setManagementContextPath(String managementContextPath) {
		super.setManagementContextPath(managementContextPath);
		this.managementContextPath = managementContextPath;
	}

	@Override
	public void setServiceContextPath(String serviceContextPath) {
		super.setServiceContextPath(serviceContextPath);
		this.serviceContextPath = serviceContextPath;
	}

	@Override
	public void setHealthEndpoint(String healthEndpoint) {
		super.setHealthEndpoint(healthEndpoint);
		this.healthEndpoint = healthEndpoint;
	}

	@Override
	protected Application convert(ServiceInstance instance) {
		String serviceUrl = append(instance.getUri().toString(), serviceContextPath);
		String managementUrl = append(instance.getUri().toString(), managementContextPath);

		String healthUrl;

		// Use health check URL from Eureka client if we have that info
		if (instance instanceof EurekaDiscoveryClient.EurekaServiceInstance) {
			EurekaDiscoveryClient.EurekaServiceInstance eurekaServiceInstance = (EurekaDiscoveryClient.EurekaServiceInstance) instance;
			healthUrl = eurekaServiceInstance.getInstanceInfo().getHealthCheckUrl();
		} else {
			healthUrl = append(managementUrl, healthEndpoint);

		}

		return Application.create(instance.getServiceId())
				.withHealthUrl(healthUrl).withManagementUrl(managementUrl)
				.withServiceUrl(serviceUrl).build();
	}
    */
}
