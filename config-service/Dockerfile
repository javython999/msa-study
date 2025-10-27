FROM openjdk:21-ea-slim
VOLUME /tmp
COPY build/libs/config-service-1.0.jar ConfigService.jar
COPY apiEncrptionKey.jks apiEncrptionKey.jks
ENTRYPOINT ["java", "-jar", "ConfigService.jar"]