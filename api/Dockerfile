FROM openjdk:11-jre-slim-buster
LABEL maintainer="vohoanglong07@gmail.com"
ADD target/api-0.0.1-SNAPSHOT.jar api.jar
ENTRYPOINT ["java","-jar","api.jar"]