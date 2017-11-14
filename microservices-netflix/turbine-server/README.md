# Turbine Server

Spring Boot application which runs the Netflix Turbine Server used for aggregating Hystrix circuit breaker metrics from all services.  Turbine (in our case) uses Eureka to find all registered services and pulls metrics from each one's `/hystrix.stream` endpoint.  It then aggregates the metrics and makes them available as a single stream at its `/turbine.stream` endpoint.  The Hystrix Dashboard can then monitor that stream in order to display metrics for all circuit breakers.

This server runs embedded Tomcat listening on port 8585 (default).  To monitor the turbine stream in Hystrix Dashboard, enter either of the following URLs in dashboard's form:

```
http://localhost:8585/tubine.stream?cluster=API-GATEWAY
http://localhost:8585/tubine.stream?cluster=PING-SERVICE
```

Although not used here, it is also possible to use AMQP to push messages to Turbine.

**For Investigation:** circuit breaker statistics are reported as clusters of services.  Currently clusters are not set up in such a way that all services are in the same cluster.  The Ping Server instances are in one cluster (PING-SERVICE) and the API Gateway services instances are in another cluster (API-GATEWAY).  They should be in a single cluster so that they can be viewed together in the Hystrix Dashboard.

To run the Turbine Server, either use the Maven Spring Boot plugin:

```
mvn spring-boot:run
```

or set up a Run Configuration in IntelliJ for this module (turbine-server) with main class `com.appdirect.service.monitoring.TurbineServer`

or build with Maven and run the JAR directly:

```
mvn clean package
java -jar target/turbine-server-1.0-SNAPSHOT.jar
```

