package com.appdirect.service.pong;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration for Pong Service.  Can be picked up from multiple sources, including application.yml for default
 * (or profile-specific) settings, or Config Server if available.
 *
 * @author Dan Nathanson
 */

@ConfigurationProperties(prefix = "pong-settings")
public class PongServiceSettings {
	/**
	 * Flag indicating service is down.  When true, all endpoint calls will throw exceptions.
	 */
	@Value("false")
	private boolean outOfOrder;

	/**
	 * Value added to all messages.
	 */
	@Value("Oops: ")  // If "Oops" shows up, something is wrong with config files and Config Server not running
	private String responseMessagePrefix;

	public boolean isOutOfOrder() {
		return outOfOrder;
	}

	public void setOutOfOrder(boolean outOfOrder) {
		this.outOfOrder = outOfOrder;
	}

	public String getResponseMessagePrefix() {
		return responseMessagePrefix;
	}

	public void setResponseMessagePrefix(String responseMessagePrefix) {
		this.responseMessagePrefix = responseMessagePrefix;
	}
}
