# 1. Стейдж сборки (Build stage)
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Кэшируем зависимости: сначала копируем только pom.xml
COPY pom.xml .
RUN mvn dependency:go-offline

# Теперь копируем остальной код и собираем
COPY src ./src
RUN mvn clean package -DskipTests

# 2. Стейдж запуска (Run stage)
FROM eclipse-temurin:17-jre
WORKDIR /app
# Копируем результат сборки из первого стейджа
COPY --from=build /app/target/*.jar app.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
