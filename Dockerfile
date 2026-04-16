# Daha küçük ve güvenli seçenek (Alpine tabanlı)
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# JAR dosyasını kopyala
COPY target/FK-0.0.1-SNAPSHOT.jar app.jar

# Cloud Run 8080 portunu bekler
EXPOSE 8080

# Uygulamayı çalıştır
ENTRYPOINT ["java", "-jar", "app.jar"]