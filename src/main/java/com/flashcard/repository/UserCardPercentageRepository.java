package com.flashcard.repository;

import com.flashcard.model.Lesson;
import com.flashcard.model.User;
import com.flashcard.model.UserCardPercentage;
import com.flashcard.model.enums.YKS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCardPercentageRepository extends JpaRepository<UserCardPercentage, Long> {

    List<UserCardPercentage> findByUser(User user);

    Optional<UserCardPercentage> findByUserAndLesson(User user, Lesson lesson);

    List<UserCardPercentage> findByLesson(Lesson lesson);

    List<UserCardPercentage> findByUserAndLessonYks(User user, YKS yks);
}
