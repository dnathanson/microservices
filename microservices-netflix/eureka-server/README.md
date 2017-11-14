# Eureka Server

Spring Boot application which runs the Netflix Eureka Server for service discovery.  Runs embedded Tomcat server on
standard Eureka port 8761.  This can be changed by setting server.port in application.yaml but will require that
all other services have their configuration updated so they know to look for Eureka Service on a non-standard port.

Eureka Server is meant to be run in a cluster with each instance on a different host (but probably all using the 
same port).  In this POC, we are only running one Eureka Server.

Eureka Server, by default, doesn't remove registered services when it doesn't hear from any instances for a 
while (e.g. a network outage).  In development, this is disabled so that instances WILL expire if Eureka Server 
doesn't hear from them (because there may only be one or to instances of any service running and it is quite likely
that none of them are up).

To run the Eureka Server, either use the Maven Spring Boot plugin:

```
mvn spring-boot:run
```

or set up a Run Configuration in IntelliJ for this module (eureka-server) with main class `com.appdirect.service.discovery.EurekaServer`

or build with Maven and run the JAR directly:

```
mvn clean package
java -jar target/eureka-server-1.0-SNAPSHOT.jar
```

