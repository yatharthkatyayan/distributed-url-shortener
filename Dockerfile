FROM eclipse-temurin:25-jre

WORKDIR /app

COPY target/distributed-url-shortener-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]