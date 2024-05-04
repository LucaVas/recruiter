# Stage 1: Build the Spring Boot application
FROM --platform=linux/amd64 maven:3.8.3-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project file and source code
COPY pom.xml /app/server/
COPY src /app/server/src

# Switch to the server folder
WORKDIR /app/server

# Build the application
RUN mvn -B package -DskipTests

# create container
FROM --platform=linux/amd64 openjdk:17-oracle

# Set the working directory inside the container
WORKDIR /app/server

# Copy the packaged JAR file from the build stage
COPY --from=build /app/server/target/recruiter-1.0-SNAPSHOT.jar ./recruiter-1.0-SNAPSHOT.jar

# expose port 3000
EXPOSE 3000

# Fire up our Spring Boot app by default
CMD ["java", "-jar", "recruiter-1.0-SNAPSHOT.jar"]
