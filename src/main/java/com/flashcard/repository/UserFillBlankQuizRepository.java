package com.flashcard.repository;

import com.flashcard.model.Topic;
import com.flashcard.model.User;
import com.flashcard.model.UserFillBlankQuiz;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFillBlankQuizRepository extends JpaRepository<UserFillBlankQuiz, Long> {
    Long countByUserAndTitle(User user, String title);

    Optional<UserFillBlankQuiz> findByUserAndTitleAndTopic(User user, @NotBlank String title, Topic topic);
}
