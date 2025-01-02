-- Veritabanını oluştur
CREATE DATABASE IF NOT EXISTS StudyHive;

-- Kullanıcı oluştur ve yetkilendir
CREATE USER IF NOT EXISTS 'StudyHive'@'%' IDENTIFIED BY 'StudyHive';
GRANT ALL PRIVILEGES ON StudyHive.* TO 'StudyHive'@'%';
FLUSH PRIVILEGES; 