package com.flashcard.repository;

import com.flashcard.model.FillBlankQuiz;
import com.flashcard.model.Topic;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FillBlankQuizRepository extends JpaRepository<FillBlankQuiz, Long> {
    List<FillBlankQuiz> findByTopic(Topic topic);

    List<FillBlankQuiz> findByTitle(String title);

    int countByTopicAndTitle(Topic topic, @NotBlank String title);

    @Query("select f from FillBlankQuiz f where f.topic = :topic")
    Page<FillBlankQuiz> findByTopicAsPage(Pageable pageable, Topic topic);
}
