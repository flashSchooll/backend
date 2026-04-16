# AŞAMA 1: Build (Maven ile derleme)
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Önce bağımlılıkları kopyala (cache için)
COPY pom.xml .
RUN mvn dependency:go-offline

# Sonra kaynak kodları kopyala ve derle
COPY src ./src
RUN mvn clean package -DskipTests

# AŞAMA 2: Runtime (Sadece JAR çalıştırma)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Build aşamasından JAR dosyasını kopyala
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]