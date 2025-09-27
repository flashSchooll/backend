package com.flashcard.repository;

import com.flashcard.model.User;
import com.flashcard.model.UserSeries;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface UserSeriesRepository extends JpaRepository<UserSeries, Long> {
    boolean existsByUserAndDate(User user, LocalDate now);
}
