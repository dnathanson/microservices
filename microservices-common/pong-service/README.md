# Pong Service

Spring Boot auto-configuration library providing RESTful microservice with some simple endpoints.  This module extracts the common code for the Pong Service and allows it to be included in Spring Boot applications that enables different techologies to be evaluated (i.e. Eureka vs. Consul). The Pong Service will be included automatically by any Spring Boot application that includes the JAR for this module on its classpath.  

AppDirectRequestContext is extracted from the HTTP header and added to Hystrix request context. At this point, the only thing in the context is JSESSION_ID which is logged in all services (just to prove context is being passed around).

Pong Service exposes some reloadable external properties available with `pong-settings` property prefix. 
* `pong-settings.responseMessagePrefix` - sets the prefix that is added to the message received on the /pong/{message} endpoint
* `pong-settings.outOfOrder` - sets boolean flag that takes the service out of order.  When this flag is `true` the service will provide DOWN as status to Eureka and will report down on the standard `/health` endpoint.  Any calls to service endpoints will throw exceptions.

Pong Service provides three endpoints. 

* `/pong/{message}` - Returns prefix from `pong-settings.responseMessagePrefix` + message as a simple String
* `/gnip/{message}` - Returns "Gnop: " + message as a simple String.
* `/ooo/{flag}` - Overrides setting of `pong-settings.outOfOrder` in real-time.  Responds with simple String indicating whether service is up or down.

