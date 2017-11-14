FROM docker.appdirectondemand.com/appdirect/java:1.0.7
MAINTAINER Platform team <platformdev@appdirect.com>

ENV APP_NAME ping-service
ENV SERVER_PORT 8082

COPY target/${APP_NAME}-*.jar /opt/
RUN find /opt -regextype egrep -regex ".*/${APP_NAME}-[0-9]+(\.[0-9])*(-SNAPSHOT)?\.jar" -exec echo {} \; -exec ln -s {} /opt/${APP_NAME}.jar \;

EXPOSE 8082

ENTRYPOINT [ "java", "-server", "-Xms384m", "-Xmx384m", "-XX:MaxMetaspaceSize=64m", "-jar", "/opt/ping-service.jar" ]
