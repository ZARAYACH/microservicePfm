FROM amazoncorretto:21-alpine-jdk

ARG JAR_FILE=target/payment-*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-Dspring.profiles.active=prod" ,"-jar","/app.jar"]
