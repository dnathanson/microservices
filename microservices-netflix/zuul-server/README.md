# Zuul 

Spring Boot application running Netflix Zuul to proxy requests to the API Gateway and Admin Server services. This is necessary to provide a well known entry point to a system where hostnames and ports of microservices are ephemeral. This application includes embedded Tomcat which listens for HTTP requests on port 8080.

Message routing is currently set up to route any request starting "/api" to a Eureka service with ID "api-gateway" (API Gateway) and any request starting /admin-server to the Admin Server. Zuul automatically wraps all calls to other services in circuit breakers using Spring Cloud's Hystrix integration. There are no retries.


![Interaction Diagram](sequencediagram.png)

To run the Zuul Server, either use the Maven Spring Boot plugin:

```
mvn spring-boot:run
```

or set up a Run Configuration in IntelliJ for this module (zuul-server) with main class `com.appdirect.service.discovery.ZuulServer`

or build with Maven and run the JAR directly:

```
mvn clean package
java -jar target/zuul-server-1.0-SNAPSHOT.jar
```

