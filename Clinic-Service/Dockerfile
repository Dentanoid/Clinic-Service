# Use an official Maven image as the base image
FROM maven:3.8.4-openjdk-17-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the project files (pom.xml and the whole src directory) to the container
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean compile assembly:single 

# Use a lightweight JRE image for the final image
FROM openjdk:17

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build image to the final image
COPY --from=build /app/target/Clinic-Service-1.0-SNAPSHOT-jar-with-dependencies.jar ./Clinic-Service-1.0-SNAPSHOT-jar-with-dependencies.jar

# Specify the command to run on container start
CMD ["java", "-jar", "Clinic-Service-1.0-SNAPSHOT-jar-with-dependencies.jar"]
