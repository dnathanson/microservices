# Ping Service

Spring Boot RESTful microservice providing some simple endpoints. This application includes embedded Tomcat which can listens on a random port. Random port is used so that two instances can be brought up on a single machine and not conflict on port bindings.  This is achieved by setting `server.port` to 0.  Port can be discovered by looking in log file or looking at Consul UI, REST API or DNS API.
 
> Note that with Brixton release, random port feature is broken.  To bring up two instances, enable Spring profiles port8002 or port8003 which will bring up instances on ports 8002 and 8003 respectively.  Or, even easier, add -Dserver.port=8002 to the command line.  This is probably more in line with what will need to be done when Dockerized.

The Ping Service features are included by simply including the microservices-common/ping-service modules JAR file in the classpath. Spring Boot autoconfiguration takes care of the rest.

For details about workings of the Ping Service, see [microservices-common/ping-service](../../microservices-common/ping-service).

To run the Ping Service, either use the Maven Spring Boot plugin:

```
mvn spring-boot:run
```

or set up a Run Configuration in IntelliJ for this module (ping-service) with main class `com.appdirect.service.ping.PingService`

or build with Maven and run the JAR directly:

```
mvn clean package
java -jar target/ping-service-1.0-SNAPSHOT.jar
```

