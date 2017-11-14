# Microservices Framework Library

This library contains some common classes to support automatically passing a AppDirectRequestContext object between services on all inter-service calls. 

Request context serialized to JSON and added to HTTP requests as a request attribute.  There is a filter that extracts and deserializes the JSON into an object and adds it as a Hystrix request variable.  Hystrix will automatically copy this variable to any child threads it uses.  There are two other classes that are used to add the serialized context to outgoing HTTP requests.  One is used on any requests that make use of Spring's RestTemplate.  The other supports use of a Feign client.

**For investigation:** Should be able to use Spring Auto Configuration to make these components are self-registering so that developers do not need to add bean definitions in every service.
