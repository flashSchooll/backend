package com.flashcard.service;

import com.flashcard.model.User;
import com.flashcard.model.UserSeries;
import com.flashcard.repository.UserRepository;
import com.flashcard.repository.UserSeriesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class UserSeriesService {
    private final UserSeriesRepository userSeriesRepository;
    private final UserRepository userRepository;

    @Transactional
    public void save(User user) {
        boolean exists = userSeriesRepository.existsByUserAndDate(user, LocalDate.now());

        if (!exists) {
            UserSeries userSeries = new UserSeries();
            userSeries.setUser(user);
            userSeries.setDate(LocalDate.now());
            userSeries.setDeleted(false);
            userSeriesRepository.save(userSeries);

            user.updateSeriesCount();
            userRepository.save(user);
        }
    }

    @Transactional
    public void resetSeries(User user) {
        user.setSeries(1);
        userRepository.save(user);
    }
}
