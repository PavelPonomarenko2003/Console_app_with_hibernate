FROM maven:3.9.4-eclipse-temurin-17 AS build
# 1. Installing working diractory
WORKDIR /app

# 2. Copy all project
COPY . .

# 3. To show maven pom.xml
RUN mvn clean package -DskipTests

# JAR-file running
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
