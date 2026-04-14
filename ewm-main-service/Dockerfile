FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml pom.xml
COPY ewm-main-service/pom.xml ewm-main-service/pom.xml
COPY ewm-stats-service/pom.xml ewm-stats-service/pom.xml
COPY ewm-stats-service/stats-dto/pom.xml ewm-stats-service/stats-dto/pom.xml
COPY ewm-stats-service/stats-client/pom.xml ewm-stats-service/stats-client/pom.xml

COPY ewm-main-service/src ewm-main-service/src
COPY ewm-stats-service/stats-dto/src ewm-stats-service/stats-dto/src
COPY ewm-stats-service/stats-client/src ewm-stats-service/stats-client/src

RUN mvn -pl ewm-main-service -am clean package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/ewm-main-service/target/ewm-main-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]