package com.appdirect.service.pong;

import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.appinfo.HealthCheckHandler;
import com.netflix.appinfo.InstanceInfo;

/**
 * Custom health checker.  Automatically picked up by Eureka and sent to Eureka Server as part of instance status.
 *
 * @author Dan Nathanson
 */
public class PongHealthCheckHandler implements HealthCheckHandler {
	@Autowired
	PongServiceSettings serviceSettings;

	@Override
	public InstanceInfo.InstanceStatus getStatus(InstanceInfo.InstanceStatus currentStatus) {

		return serviceSettings.isOutOfOrder() ? InstanceInfo.InstanceStatus.DOWN : InstanceInfo.InstanceStatus.UP;
	}
}
