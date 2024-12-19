package com.flashcard.repository;

import com.flashcard.model.*;
import com.flashcard.model.enums.YKS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByFlashcard(Flashcard flashcard);

    int countByFlashcard(Flashcard flashcard);

    @Query("SELECT COUNT(c) FROM Card c WHERE c.flashcard.topic.lesson = :lesson")
    int countByFlashcardTopicLesson(@Param("lesson") Lesson lesson);

    @Query("SELECT c FROM Card c WHERE c.flashcard.topic.lesson = :lesson")
    List<Card> findByLesson(Lesson lesson);

    @Query("SELECT c FROM Card c WHERE c.flashcard.topic = :topic")
    List<Card> findByTopic(Topic topic);


    int countByFlashcardTopicLessonYks(YKS yks);


}
