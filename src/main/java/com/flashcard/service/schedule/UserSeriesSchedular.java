package com.flashcard.service.schedule;

import com.flashcard.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class UserSeriesSchedular {
    private final Logger log = LoggerFactory.getLogger(UserSeriesSchedular.class);

    private final UserRepository userRepository;

    @Scheduled(cron = "0 1 0 * * *")
    @Transactional
    public void resetUserSeriesIfInactive() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        int updatedCount = userRepository.resetSeriesForInactiveUsers(yesterday);
        log.info("Serisi sıfırlanan kullanıcı sayısı: " + updatedCount);
    }
}
