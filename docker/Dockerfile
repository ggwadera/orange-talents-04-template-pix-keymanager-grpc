FROM openjdk:16-alpine
WORKDIR /home/app
COPY layers/libs .
COPY layers/resources .
COPY layers/application.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/home/app/application.jar"]
