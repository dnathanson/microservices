# Admin Server

Spring Boot auto-configuration library for Spring Cloud Admin Server from Codecentric AG which provides a UI for managing services that are build upon Spring Cloud platform.  Supports using spring discovery client to find all services.  Periodically pings services for health.  Can look also get metrics and environment info from services and even make temporary changes to service configuration properties.

The Admin Server will be included automatically by any Spring Boot application that includes the JAR for this module on its classpath.  

**For investigation:** Admin Server instances can form a cluster if more than one is run and Hazelcast is found on the classpath.  Not sure what this clustering does.


