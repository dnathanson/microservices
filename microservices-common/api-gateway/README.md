# API Gateway

Spring Boot auto-configuration library providing RESTful API to the rest of the services (Ping Server and Pong Server) in the system.  This module extracts the common code for the API Gateway and allows it to be included in Spring Boot applications that enables different technologies to be evaluated (i.e. Eureka vs. Consul). The API Gateway will be included automatically by any Spring Boot application that includes the JAR for this module on its classpath.  

All calls to other services are wrapped in circuit breakers using Spring Cloud's Hystrix integration.  Failed calls are retried (to handle case where single service instance might have failed) within the scope of the hystrix request.  If all retries fail, the circuit breakers failure count is incremented and eventually the circuit will open.

There are examples of two types of clients (Ribbon and Feign) provided, both of which make use of service discovery to find the host / port of target services and do client-side load balancing between instances.  It is Ribbon that is actually doing the service discovery and Feign uses Ribbon under the covers. The Ribbon client is provided in the client libraries for Ping Service and Pong Service, ping-client and pong-client, respectively.

> NOTE: With Spring Cloud Angel.S3 release, the Feign client was working perfectly.  With the Brixton.BUILD-SNAPSHOT release that is currently used in the pom.xml file, the Feign client doesn't work as it used to - it now has it's own circuit breaker and retry logic which conflicts with what is implemented here.

There is a filter (`AppDirectRequestContextInitializationFilter`) which grabs the JSESSION from the HTTP request and adds it to the `AppDirectRequestContext`.  The `AppDirectRequestContext` is a Hystrix context variable is automatically propagated into Hystrix child threads. There are two other extensions (part of common microservices-framework library) that ensure that context is passed between services automagically.

API Gateway provides proxies to all the endpoints of Ping Service and Pong Service. For each of those it simply add the prefix "API: " to the message returned from those services and return the concatenated string as it's response.

```
/api/ping/{message}
/api/gnip/{message}
/api/both/{message}
/api/pong/{message}
/api/gnop/{message}
```

So, /api/ping/foo will return "API: Ping: Pong: foo" (because Ping Service /ping endpoint calls Pong Service /pong endpoint and those endpoints simply add the prefix "Ping: " and "Pong: " to the message, respectively.

There is also an example of orchestrating requests to two services and aggregating the responses.  This endpoint provides a JSON response.  The calls to the two services are made in parallel and the responses are handled using RxJava Observables.

/api/pingpong/{message}

which returns (for message "foo"):
```json
{
    "ping": "Ping: Pong: foo",
    "pong": "Pong: foo"
}
```
