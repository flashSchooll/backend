# AŞAMA 1: Build (Maven + JDK)
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Önce bağımlılıkları indir (cache için)
COPY pom.xml .
RUN mvn dependency:go-offline || true

# Kaynak kodları kopyala ve derle
COPY src ./src
RUN mvn clean package -DskipTests

# AŞAMA 2: Runtime (Sadece JRE)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Build aşamasından JAR'ı kopyala
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]