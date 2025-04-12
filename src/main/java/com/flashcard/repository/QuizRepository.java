package com.flashcard.repository;

import com.flashcard.model.Quiz;
import com.flashcard.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByTopic(Topic topic);

    List<Quiz> findByNameAndTopic(String name, Topic topic);

    int countByNameAndTopic(String name,Topic topic);
}
