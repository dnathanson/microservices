package com.appdirect.service.ping;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.appdirect.service.pong.client.PongClient;


/**
 * REST Client for Pong Service, based on Spring Cloud and Netflix Feign, that sends messages to Pong Service.
 * Pong Service is found using discovery service.
 * <p>
 * All calls are synchronous.  Is up to the user of the library to implement asynchronous execution if desired.
 * <p>
 * Feign uses Ribbon & Hystrix under the covers, so retry, service discovery and circuit breakers are automatic.  However,
 * there is currently no way to define Hystrix fallback methods when circuits are open. If fallback behavior is
 * desired, use HystrixWrapper + RetryWrapper pattern.
 *
 * @author Dan Nathanson.
 */
@FeignClient("pong-service")
public interface PongFeignClient extends PongClient {
}
