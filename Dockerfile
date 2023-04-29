FROM maven:latest as base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN mvn dependency:resolve
COPY src ./src

FROM base as test
RUN mvn test

FROM base as build
RUN mvn package

FROM openjdk:17-alpine as run
EXPOSE 8080
COPY --from=build /app/target/linkShortener.jar /app.jar
CMD ["java", "-jar", "/app.jar"]