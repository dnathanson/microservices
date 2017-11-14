# Congif Server

Spring Boot application which runs the Spring Cloud Config Server for support of externalized (central) configuration. Runs embedded Tomcat server on standard Config Server port 8888.  This can be changed by setting server.port in application.yaml but will require that all other services have their configuration updated so they know to look for Config Service on a non-standard port using `spring.cloud.config.uri` property.

The Config Server reads configuration data from a Git repository or the local file system.  Currently (from bootstrap.yaml) it is looking in user's home directory for a directory called `/src/appdirect-config` where it expects to find configuration files organized by service and environment.  This is actually a repo that you can clone locally although you could change the config for the Config Server and it will clone the repo automatically. 
 
For instance there are currently files there called:
```
pong-service.yaml
application.yaml
```

If the Pong Service asks for it's config, it will get a response that contains a merged set of properties from those two files.  Any other service would get only the properties from `application.yaml`.  

When spring-cloud-bus is found on the classpath, property changes (or at least notification of changes) can be propagated to all services by hitting the /refresh endpoint any any service.

Config Server has no UI but you can play with the REST API.  Try:

http://localhost:8888/pong-service/dev


You should get the following JSON response containing set of properties and property sources for the Pong Service which includes properties from application.yaml and pong-service.yaml.  If there had been a pong-service-dev.yaml file, it would have been included, too.

```json

{
    "name": "pong-service",
    "profiles": [
        "dev"
    ],
    "label": null,
    "version": null,
    "propertySources": [
        {
            "name": "file:/Users/dan.nathanson/src/appdirect-config/pong-service.yml",
            "source": {
                "pong-settings.responseMessagePrefix": "Pong: ",
                "pong-settings.outOfOrder": false
            }
        },
        {
            "name": "file:/Users/dan.nathanson/src/appdirect-config/application.yaml",
            "source": {
                "endpoints.health.sensitive": false
            }
        }
    ]
}
```
**For investigation:** Config Server can serve up text files in the same way so, for instance, a single logback.xml could be hosted on the ConfigServer.

**For investigation:** How can we leverage Spring Cloud Config support for different patterns for organizing files in the git repo to support different environments and possibly different channels using multiple repos.  See http://cloud.spring.io/spring-cloud-config/spring-cloud-config.html. 

To run the Config Server, either use the Maven Spring Boot plugin:

```
mvn spring-boot:run
```

or set up a Run Configuration in IntelliJ for this module (config-server) with main class `com.appdirect.service.config.ConfigServer`

or build with Maven and run the JAR directly:

```
mvn clean package
java -jar target/config-server-1.0-SNAPSHOT.jar
```

**Copy the files from /src/config/ to ${user.home}/src/appdirect-config**
