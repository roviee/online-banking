## Use Java 17 as base
#FROM eclipse-temurin:17-jdk-alpine
#
## Set working directory
#WORKDIR /app
#
## Copy Maven/Gradle wrapper and dependencies
#COPY mvnw .
#COPY .mvn .mvn
#COPY pom.xml .
#
## Download dependencies (this layer will be cached)
#RUN ./mvnw dependency:go-offline
#
## Copy source code
#COPY src src
#
## Build the application
#RUN ./mvnw package -DskipTests
#
## Run the application
#ENTRYPOINT ["java", "-jar", "target/*.jar"]