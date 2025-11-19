package com.flashcard.service.schedule;

import com.flashcard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WeeklyStarSchedular {
    private final UserRepository userRepository;

    @Scheduled(cron = "1 0 0 * * MON", zone = "Europe/Istanbul")
    public void updateWeeklyStar(){
        userRepository.updateWeeklyStar();
    }

}
