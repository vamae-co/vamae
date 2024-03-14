FROM alpine:3.19.1
RUN apk add openjdk21
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]