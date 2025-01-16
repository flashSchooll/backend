package com.flashcard.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Asia/Istanbul zaman dilimini belirleyin
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Istanbul"));

        // Eğer tarih formatı belirtmek isterseniz
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        // Jackson'ı ISO 8601 formatına göre serileştirme için yapılandırabilirsiniz
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }
}
