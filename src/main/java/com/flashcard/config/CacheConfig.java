package com.flashcard.config;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        // SimpleCacheManager ile basit bir cache yapılandırması
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<Cache> caches = new ArrayList<>();

        // 'yksLessonCache' adında bir cache oluşturuluyor
        caches.add(new ConcurrentMapCache("yksLesson"));
        caches.add(new ConcurrentMapCache("lessonTopic"));
        caches.add(new ConcurrentMapCache("flashcardCard"));
        caches.add(new ConcurrentMapCache("topicSummary"));
        caches.add(new ConcurrentMapCache("topicSummaries"));
        caches.add(new ConcurrentMapCache("users"));
        cacheManager.setCaches(caches);
        return  cacheManager;
    }

}