FROM maven:3.9.8-amazoncorretto-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests

FROM amazoncorretto:17.0.14

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

# Create image
# docker build -t airbnb-app .

# Run container from image
# docker run -p 8080:8080 airbnb

# docker build -t qhaofdev/airclone-app:1.0.1 .
