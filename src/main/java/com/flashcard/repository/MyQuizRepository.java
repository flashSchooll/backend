package com.flashcard.repository;

import com.flashcard.model.MyQuiz;
import com.flashcard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyQuizRepository extends JpaRepository<MyQuiz, Long> {
    List<MyQuiz> findByUser(User user);
}
