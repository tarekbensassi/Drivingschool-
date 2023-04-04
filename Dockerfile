#docker
FROM maven:3.8.6-jdk-8-alpine AS build
# Build Stage
MAINTAINER Tarek Bensassi
WORKDIR /easysender
COPY /easysender .
RUN mvn clean install -DskipTests
# Docker Build Stage
FROM openjdk:8-jdk-alpine

ADD --from=build /easysender/target/*.jar easysender.jar
#COPY target/*.jar easysender.jar
ENV PORT 9024
EXPOSE $PORT

ENTRYPOINT ["java","-jar","-Xmx1024M","-Dserver.port=${PORT}","easysender.jar"]

