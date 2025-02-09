package com.flashcard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com"); // SMTP sunucusu
        mailSender.setPort(587); // SMTP portu

        // E-posta göndermek için gerekli kimlik doğrulama bilgileri
        mailSender.setUsername("infostumy@gmail.com"); // Mail hesabı kullanıcı adı
        mailSender.setPassword("ilebdcfoujfeitgq"); // Mail hesabı şifresi

        // E-posta gönderiminde kullanılacak mail özellikleri (isteğe bağlı)
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");


        return mailSender;
    }
}
