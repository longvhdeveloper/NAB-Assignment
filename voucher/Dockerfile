FROM openjdk:11-jre-slim-buster
LABEL maintainer="vohoanglong07@gmail.com"
ADD target/voucher-0.0.1-SNAPSHOT.jar voucher.jar
ENTRYPOINT ["java","-jar","voucher.jar"]