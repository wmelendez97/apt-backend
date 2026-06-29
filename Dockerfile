# Build stage for production or final image
FROM maven:3.9.6-amazoncorretto-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Development stage for hot-reloading
FROM maven:3.9.6-amazoncorretto-11 AS dev
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Final stage for production
FROM amazoncorretto:11-alpine
WORKDIR /app
COPY --from=build /app/target/apt-backend.war app.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]