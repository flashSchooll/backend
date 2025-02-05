package com.flashcard.repository;

import com.flashcard.model.User;
import com.flashcard.model.UserQuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserQuizAnswerRepository extends JpaRepository<UserQuizAnswer,Long> {
    List<UserQuizAnswer> findByUserAndQuizName(User user, String name);


    boolean existsByUserAndQuizName(User user, String name);
}
