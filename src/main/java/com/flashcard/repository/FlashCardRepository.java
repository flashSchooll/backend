package com.flashcard.repository;

import com.flashcard.model.Flashcard;
import com.flashcard.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FlashCardRepository extends JpaRepository<Flashcard, Long> {

    Page<Flashcard> findByTopic(Topic topic, Pageable pageable);

    List<Flashcard> findByTopic(Topic topic);

    boolean existsByCardName(String cardName);


    @Query("select f from Flashcard f " +
            "where (f.cardName ilike (%:search%) " +
            "or f.topic.subject ilike (%:search%))")
    List<Flashcard> search(@Param("search") String search);

    Optional<Flashcard> findByCardNameAndTopic(String flashCardName, Topic topic);

    @Query("SELECT f, COUNT(c) " +
            "FROM Flashcard f " +
            "LEFT JOIN Card c ON c.flashcard.id = f.id " +
            "WHERE f.topic = :topic " +
            "GROUP BY f")
    List<Object[]> findFlashcardsWithCountByTopic(Topic topic);
}
