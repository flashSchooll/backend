package com.flashcard.repository;

import com.flashcard.model.*;
import com.flashcard.model.enums.Branch;
import com.flashcard.model.enums.YKS;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {


    List<Card> findByFlashcard(Flashcard flashcard);

    @Query("select c from Card c where c.flashcard = :flashcard")
    @EntityGraph("card-graph")
    List<Card> findCardsWithFlashcard(Flashcard flashcard);


    int countByFlashcard(Flashcard flashcard);

    @Query("SELECT COUNT(c) FROM Card c WHERE c.flashcard.topic.lesson = :lesson")
    int countByFlashcardTopicLesson(@Param("lesson") Lesson lesson);

    @Query("SELECT c FROM Card c WHERE c.flashcard.topic.lesson = :lesson")
    List<Card> findByLesson(Lesson lesson);

    @Query("SELECT c FROM Card c WHERE c.flashcard.topic = :topic")
    List<Card> findByTopic(Topic topic);


    int countByFlashcardTopicLessonYks(YKS yks);

    @Query(value = "SELECT c.* FROM Card c " +
            "JOIN Flashcard f ON c.flashcard_id = f.id " +
            "JOIN Topic t ON f.topic_id = t.id " +
            "JOIN Lesson l ON t.lesson_id = l.id " +
            "WHERE (l.branch is null or (:branch is null or l.branch = :branch)) " +
            "ORDER BY RANDOM() LIMIT 100", nativeQuery = true)
    List<Card> findRandomCardsByBranch(@Param("branch") String branch);

    @Query("SELECT DISTINCT c FROM Card c " +
            "JOIN FETCH c.flashcard f " +
            "WHERE f.id IN :flashcardIds")
    List<Card> findByFlashcardIn(List<Long> flashcardIds);

    Long countByFlashcardTopic(Topic topic);

    int countByFlashcardTopicLessonYksAndFlashcardTopicLessonBranch(YKS yks, Branch branch);

    @Query("SELECT c FROM Card c " +
            "JOIN FETCH c.flashcard f " +
            "JOIN FETCH f.topic t " +
            "JOIN FETCH t.lesson l " +
            "WHERE l.yks = :yks")
    List<Card> findByFlashcardTopicLessonYks(@Param("yks") YKS yks);

    @Query("SELECT c FROM Card c " +
            "JOIN FETCH c.flashcard f " +
            "JOIN FETCH f.topic t " +
            "JOIN FETCH t.lesson l " +
            "WHERE l.yks = :yks " +
            "and l.branch = :branch")
    List<Card> findByFlashcardTopicLessonYksBranch(YKS yks, Branch branch);
}
