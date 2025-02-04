package com.flashcard.repository;

import com.flashcard.model.FillBlankQuiz;
import com.flashcard.model.Topic;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FillBlankQuizRepository extends JpaRepository<FillBlankQuiz,Long> {
    List<FillBlankQuiz> findByTopic(Topic topic);

    List<FillBlankQuiz> findByTitle(String title);

    int countByTopicAndTitle(Topic topic, @NotBlank String title);
}
