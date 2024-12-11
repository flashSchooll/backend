package com.flashcard.repository;

import com.flashcard.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserSeenCardRepository extends JpaRepository<UserSeenCard, Long> {

    List<UserSeenCard> findByUserAndCardFlashcard(User user, Flashcard flashcard);

    List<UserSeenCard> findByUserAndCardFlashcardAndStateOfKnowledgeIsFalse(User user, Flashcard flashcard);

    @Query("select u.card from UserSeenCard u " +
            "where " +
            "u.user = :user " +
            "and (:stateOfKnowledge is null or u.stateOfKnowledge = :stateOfKnowledge)")
    List<Card> findByUser(User user, Boolean stateOfKnowledge);

    List<UserSeenCard> findByUserAndCardFlashcardAndStateOfKnowledgeIsTrue(User user, Flashcard flashcard);

    List<UserSeenCard> findByUserAndCardFlashcardTopic(User user, Topic topic);
}
