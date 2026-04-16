# Temel imaj olarak OpenJDK kullan
FROM openjdk:17-jdk-slim

# Çalışma dizinini belirle
WORKDIR /app

# Uygulama JAR dosyasını kopyala
ARG JAR_FILE=target/FK-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Port ayarı
EXPOSE 8080

# Uygulamayı çalıştır
ENTRYPOINT ["java", "-jar", "/app/app.jar"] 