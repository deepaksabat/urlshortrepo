FROM openjdk:17-alpine
EXPOSE 8080
COPY target/urlshortner.jar urlshortner.jar
ENTRYPOINT ["java","-jar","/urlshortner.jar"]