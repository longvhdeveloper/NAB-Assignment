FROM openjdk:11-jre-slim-buster
LABEL maintainer="vohoanglong07@gmail.com"
ADD target/customer-0.0.1-SNAPSHOT.jar customer.jar
ENTRYPOINT ["java","-jar","customer.jar"]