# Client Library for Pong Service

This is a client library for the Pong Service. It contains an implementation of a REST Client (Using Ribbon) that can be used to send messages to the Ping Service. There is a method on the client for each exposed endpoint on the service.  To use the client, simple add a dependency on the module and define a bean of type `PongRibbonClient`.  The client will autowire a `RestTemplate` that is Ribbon/Eureka aware. The application needs to have `@EnableDiscoveryClient` (or `@EnableEurekaClient`) annotation present.

Responses from the Pong Service are not simple strings.  They return JSON response objects that contain a message and a request ID.  This library provides Java class implementations for /pong response (`PongResponse`) and /gnop (`GnopResponse`) that are part of the API.

There is also an interface supplied with REST annotations that defines the service's REST API.  This interface can be used to create a Feign client by doing something as simple as:

```java
@FeignClient("pong-service")
public interface PongFeignClient extends PongClient {
}
```

**For investigation:** The above Feign client declaration works with Spring Cloud Angel.S3 release.  Not sure what needs to be done with Brixton release since the Feign client now uses Hystrix and so fallback methods should be defined for each of the client API methods.

