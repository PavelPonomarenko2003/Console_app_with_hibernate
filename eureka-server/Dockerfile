FROM bellsoft/liberica-openjdk-alpine:17
COPY target/*-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]