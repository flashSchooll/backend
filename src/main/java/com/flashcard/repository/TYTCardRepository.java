package com.flashcard.repository;

import com.flashcard.model.TYTCard;
import com.flashcard.model.TYTFlashcard;
import com.flashcard.model.TYTLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TYTCardRepository extends JpaRepository<TYTCard, Long> {

    List<TYTCard> findByTytFlashcard(TYTFlashcard flashcard);

    int countByTytFlashcard(TYTFlashcard flashcard);

    @Query("SELECT COUNT(c) FROM TYTCard c WHERE c.tytFlashcard.topic.tytLesson = :lesson")
    int countByTYTFlashcardTopicTytLesson(@Param("lesson") TYTLesson lesson);

  /*  @Query("SELECT new com.flashcard.controller.tytlesson.user.response." +
            "TYTLessonCardSeenCountResponse(c.tytFlashcard.topic.tytLesson, COUNT(c)) " +
            "FROM TYTCard c " +
            "GROUP BY c.tytFlashcard.topic.tytLesson")
    List<TYTLessonCardSeenCountResponse> countCardsByLesson();

   */


}
