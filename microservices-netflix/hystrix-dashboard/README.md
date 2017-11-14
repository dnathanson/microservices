# Hystrix Dashboard Server

Spring Boot application which runs the Netflix Hystrix Dashboard used for monitoring the state of a cluster's circuit breakers in near real-time.   Runs embedded Tomcat server on standard port 8989.  This can be changed by setting server.port in bootstrap.yaml.

In order to display data, you need to supply the URL for a Hystrix stream.  Any service that has @EnableCircuitBreakers will make a /hystrix.stream endpoint available.  To display info for the circuit breakers in that service, the Hystrix Dashboard reads that stream.

Go to http://localhost:8989/hystrix which will display the Hystrix Dashboard landing page which has a form field where the URL of a hystrix stream can be entered.  There are examples on the form.

When Turbine is in use (see `turbine-server`), circuit breaker statistics for clusters of services can be aggregated together in the dashboard by monitoring the stream from the Turbine Server.


To run the Hystrix Dashboard, either use the Maven Spring Boot plugin:

```
mvn spring-boot:run
```

or set up a Run Configuration in IntelliJ for this module (hystrix-dashboard) with main class `com.appdirect.service.monitoring.HystrixDashboardServer`

or build with Maven and run the JAR directly:

```
mvn clean package
java -jar target/hystrix-dashboard-1.0-SNAPSHOT.jar
```

