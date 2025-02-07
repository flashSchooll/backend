package com.flashcard.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@EnableCaching
@ComponentScan
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        final SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(
                Arrays.asList(
                        new ConcurrentMapCache("lessonTopic"),
                        new ConcurrentMapCache("cardsCache"),
                     //   new ConcurrentMapCache("users"),
                        new ConcurrentMapCache("cardCache"),
                    //    new ConcurrentMapCache("contactMessages"),
                        new ConcurrentMapCache("allCards"),
                     //   new ConcurrentMapCache("fillBlankQuizes"),
                      //  new ConcurrentMapCache("flashcardSearch"),
                    //    new ConcurrentMapCache("fillBlankQuizesByTitle"),
                        new ConcurrentMapCache("myCards")
                    //    new ConcurrentMapCache("myQuizes"),
                    //    new ConcurrentMapCache("quizCounts")
                   //    new ConcurrentMapCache("userQuizAnswers")
                      //  new ConcurrentMapCache("countAverageFifty"),
                        //    new ConcurrentMapCache("userCardPercentageAyt"),
                        //     new ConcurrentMapCache("userCardPercentageTyt"),
                      //  new ConcurrentMapCache("topicSummaries")
                ));
        return cacheManager;
    }
}