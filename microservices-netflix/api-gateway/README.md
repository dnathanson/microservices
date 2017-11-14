# API Gateway

Spring Boot application providing RESTful API to the rest of the services (Ping Server and Pong Server) in the system. This application included embedded Tomcat which listens for HTTP requests on port 9999.  

The API Gateway features are included by simply including the microservices-common/api-gateway modules JAR file in the classpath. Spring Boot autoconfiguration takes care of the rest.

For details about workings of the API Gateway, see [microservices-common/api-gateway](../../microservices-common/api-gateway).

To run the API Gateway, either use the Maven Spring Boot plugin:

```
mvn spring-boot:run
```

or set up a Run Configuration in IntelliJ for this module (api-gateway) with main class `com.appdirect.appdirect.service.gateway.ApiGateway`

or build with Maven and run the JAR directly:

```
mvn clean package
java -jar target/api-gateway-1.0-SNAPSHOT.jar
```

