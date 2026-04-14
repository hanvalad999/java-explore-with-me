FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml pom.xml
COPY ewm-main-service/pom.xml ewm-main-service/pom.xml
COPY ewm-stats-service/pom.xml ewm-stats-service/pom.xml
COPY ewm-stats-service/stats-dto/pom.xml ewm-stats-service/stats-dto/pom.xml
COPY ewm-stats-service/stats-client/pom.xml ewm-stats-service/stats-client/pom.xml
COPY ewm-stats-service/stats-server/pom.xml ewm-stats-service/stats-server/pom.xml

COPY ewm-stats-service/stats-dto/src ewm-stats-service/stats-dto/src
COPY ewm-stats-service/stats-client/src ewm-stats-service/stats-client/src
COPY ewm-stats-service/stats-server/src ewm-stats-service/stats-server/src

RUN mvn -pl ewm-stats-service/stats-server -am clean package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/ewm-stats-service/stats-server/target/stats-server.jar app.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]