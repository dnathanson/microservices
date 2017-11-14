package com.appdirect.service.admin;

import org.springframework.context.annotation.Configuration;

import de.codecentric.boot.admin.config.EnableAdminServer;

/**
 * Auto-configuring bean for Spring Admin Server.  If the microservices-common/admin-server JAR is on the classpath
 * of a Spring Boot application, that application will automatically run the Admin Server.
 *
 * @author Dan Nathanson.
 */
@EnableAdminServer
@Configuration
public class AdminServer {
}
