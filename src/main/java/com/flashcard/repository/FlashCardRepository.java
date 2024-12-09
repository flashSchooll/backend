package com.flashcard.repository;

import com.flashcard.model.Flashcard;
import com.flashcard.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FlashCardRepository extends JpaRepository<Flashcard, Long> {

    List<Flashcard> findByTopic(Topic topic);

    boolean existsByCardName(String cardName);


    @Query("select f from Flashcard f " +
            "where (f.cardName ilike (%:search%) " +
            "or f.topic.subject ilike (%:search%))")
    List<Flashcard> search(@Param("search") String search);
}
