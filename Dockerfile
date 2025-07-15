FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/linkfix-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_linkfix.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_linkfix.jar"]
