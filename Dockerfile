# Build
FROM maven:3.9.6-amazoncorretto-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Final
FROM amazoncorretto:11-alpine
WORKDIR /app
COPY --from=build /app/target/apt-backend.war app.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]