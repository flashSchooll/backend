package com.flashcard.repository;

import com.flashcard.model.User;
import com.flashcard.model.UserFillBlankQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFillBlankQuizRepository extends JpaRepository<UserFillBlankQuiz, Long> {
    Long countByUserAndTitle(User user, String title);
}
