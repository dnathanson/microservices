# Ping Service

Spring Boot auto-configuration library providing RESTful microservice with some simple endpoints.  This module extracts the common code for the Ping Service and allows it to be included in Spring Boot applications that enables different technologies to be evaluated (i.e. Eureka vs. Consul). The Ping Service will be included automatically by any Spring Boot application that includes the JAR for this module on its classpath.  

All calls to other services are wrapped in circuit breakers using Spring Cloud's Hystrix integration.  Failed calls are retried (to handle case where single service instance might have failed) within the scope of the hystrix request.  If all retries fail, the circuit breakers failure count is incremented and eventually the circuit will open.

There are examples of two types of clients (Ribbon and Feign) provided, both of which make use of service discovery to find the host / port of target services and do client-side load balancing between instances.  It is Ribbon that is actually doing the service discovery and Feign uses Ribbon under the covers. The Ribbon client is provided in the client libraries for Ping Service and Pong Service, ping-client and pong-client, respectively.

> NOTE: With Spring Cloud Angel.S3 release, the Feign client was working perfectly.  With the Brixton.BUILD-SNAPSHOT release that is currently used in the pom.xml file, the Feign client doesn't work as it used to - it now has it's own circuit breaker and retry logic which conflicts with what is implemented here.

AppDirectRequestContext is extracted from the HTTP header and added to Hystrix request context.  It is added automatically to the outgoing HTTP request headers.  At this point, the only thing in the context is JSESSION_ID which is logged in all services (just to prove context is being passed around).

Ping Service provides three endpoints. Each of those endpoints makes an additional call (or calls) to the Pong Service.

* `/ping/{message}` - Calls Pong Service's `/pong/{message}` endpoint.  The prefix "Ping: " is added to the response from the Pong Service and is returned as a simple String response.
* `/gnip/{message}` - Calls Pong Service's `/gnop/{message}` endpoint.  The prefix "Gnip: " is added to the response from the Pong Service and is returned as a simple String response.
* `/both/{message}` - Calls both the `/pong/{message}` and `/gnop/{message}` endpoints of the Pong Service.  RxJava Observables are used to make calls in parallel.  Hystrix uses separate threads for requests to Pong Service.  Observable.observeOn(..) is used to process responses on new thread as well.  "Both: " prefix is added to concatenation of response from `/pong` and `/gnop` and returned as a simple String.
