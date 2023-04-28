FROM maven:latest as build
WORKDIR /app
COPY pom.xml .
COPY src/ /app/src/
RUN mvn clean package

FROM openjdk:17-alpine as run
WORKDIR /app
COPY --from=build /app/target/linkShortener.jar .
CMD ["java", "-jar", "linkShortener.jar"]
