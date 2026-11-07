# Stage 1: build the Angular app. angular.json writes it to ../resources/static
FROM node:22-slim AS frontend
WORKDIR /build/src/main/app
COPY src/main/app/package.json src/main/app/package-lock.json ./
RUN npm ci --no-audit --no-fund
COPY src/main/app ./
RUN npx ng build

# Stage 2: build the Spring Boot jar with the freshly built frontend
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
COPY .mvn .mvn
RUN mvn -B -q dependency:go-offline
COPY src ./src
COPY --from=frontend /build/src/main/resources/static ./src/main/resources/static
RUN mvn -B -q clean package -DskipTests

# Stage 3: runtime
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/target/portfoliomanager-0.0.1-SNAPSHOT.jar portfoliomanager.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","portfoliomanager.jar"]
