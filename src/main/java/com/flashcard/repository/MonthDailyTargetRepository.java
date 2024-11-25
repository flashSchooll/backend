package com.flashcard.repository;

import com.flashcard.model.MonthDailyTarget;
import com.flashcard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonthDailyTargetRepository extends JpaRepository<MonthDailyTarget,Long> {

    List<MonthDailyTarget> findByUser(User user);
}
