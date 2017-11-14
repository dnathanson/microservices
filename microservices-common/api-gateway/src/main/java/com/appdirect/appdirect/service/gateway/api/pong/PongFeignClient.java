package com.appdirect.appdirect.service.gateway.api.pong;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.appdirect.service.pong.client.PongClient;


/**
 * REST Client for Pong Service, based on Spring Cloud and Netflix Feign, that sends messages to Pong Service.
 * Pong Service is found using a discovery service.
 * <p>
 * All calls are synchronous.  Is up to the user of the library to implement asynchronous execution if desired.
 *
 * @author Dan Nathanson.
 */
@FeignClient("pong-service")
public interface PongFeignClient extends PongClient {
}
