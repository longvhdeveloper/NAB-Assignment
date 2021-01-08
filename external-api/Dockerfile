FROM openjdk:11-jre-slim-buster
LABEL maintainer="vohoanglong07@gmail.com"
ADD target/external-api-0.0.1-SNAPSHOT.jar external-api.jar
ENTRYPOINT ["java","-jar","external-api.jar"]