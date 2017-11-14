# Admin Server

Spring Boot application which runs the Spring Cloud Admin Server from Codecentric AG which provides a UI for managing services that are build upon Spring Cloud platform.  Supports using spring discovery client to find all services.  Periodically pings services for health.  Can look also get metrics and environment info from services and even make temporary changes to service configuration properties.

By default, runs on port 8001.  This can be changes by setting `server.port`. The UI is available at root context.

The Admin Server features are included by simply including the microservices-common/admin-server modules JAR file in the classpath. Spring Boot autoconfiguration takes care of the rest.

For details about workings of the Admin Server, see [microservices-common/admin-server](../../microservices-common/admin-server).

To run the Admin Server, either use the Maven Spring Boot plugin:

```
mvn spring-boot:run
```

or set up a Run Configuration in IntelliJ for this module (admin-server) with main class `com.appdirect.service.admin.AdminServer`

or build with Maven and run the JAR directly:

```
mvn clean package
java -jar target/admin-server-1.0-SNAPSHOT.jar
```

