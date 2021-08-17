FROM adoptopenjdk/openjdk11:alpine-slim

COPY ./target/restaurant-reservation-service-0.0.1-SNAPSHOT.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch restaurant-reservation-service-0.0.1-SNAPSHOT.jar'

ENTRYPOINT ["java","-jar","restaurant-reservation-service-0.0.1-SNAPSHOT.jar"]