
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# -------- Runtime stage --------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the JAR file from build stage
COPY --from=build /app/target/*.jar config.jar

# Start the Config Server
ENTRYPOINT ["java", "-jar", "config.jar"]
