FROM openjdk:11-jre-slim-buster
LABEL maintainer="vohoanglong07@gmail.com"
ADD target/sms-0.0.1-SNAPSHOT.jar sms.jar
ENTRYPOINT ["java","-jar","sms.jar"]