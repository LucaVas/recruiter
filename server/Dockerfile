# Docker multi-stage build
FROM --platform=linux/amd64 maven:3-jdk-11 as builder

# set workdir as app
WORKDIR /app

# copy all files
COPY . .

# Just echo so we can see, if everything is there :)
RUN ls -l

# Run Maven build
RUN mvn clean install

# Just using the build artifact and then removing the build-container
FROM openjdk:17.0.2-jdk

VOLUME /tmp

ARG JAR_FILE=target/recruiter-server.jar

ENV JAVA_OPTIONS=""

ADD ${JAR_FILE} app.jar

# expose port 8080
EXPOSE 8080

# Fire up our Spring Boot app by default
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
