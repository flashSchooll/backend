FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Görseldeki doğru JAR adını kullan
COPY target/api-0.0.1-v1.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]