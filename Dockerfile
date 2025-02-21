FROM openjdk:17

ARG FILE_JAR=target/airbnb-0.0.1-SNAPSHOT.jar

ADD ${FILE_JAR} api-airbnb.jar

ENTRYPOINT ["java", "-jar", "api-airbnb.jar"]

EXPOSE 8080

# Create image
# docker build -t my-airbnb-app .

#Run container from image
# docker run -p 8080:8080 airbnb

# => Define image Docker