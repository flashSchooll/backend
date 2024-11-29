package com.flashcard.repository;

import com.flashcard.model.TYTFlashcard;
import com.flashcard.model.TYTTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TYTFlashCardRepository extends JpaRepository<TYTFlashcard, Long> {

    List<TYTFlashcard> findByTopic(TYTTopic topic);

    boolean existsByCardName(String cardName);


    @Query("select f from TYTFlashcard f " +
            "where (f.cardName ilike (%:search%) " +
            "or f.topic.subject ilike (%:search%) " +
            "or f.topic.tytLesson.tyt ilike (%:search%))")
    List<TYTFlashcard> search(String search);
}
