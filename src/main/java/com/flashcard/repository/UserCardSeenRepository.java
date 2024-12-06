package com.flashcard.repository;

import com.flashcard.model.Card;
import com.flashcard.model.Flashcard;
import com.flashcard.model.User;
import com.flashcard.model.UserSeenCard;
import com.flashcard.model.enums.DifficultyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCardSeenRepository extends JpaRepository<UserSeenCard, Long> {

    List<UserSeenCard> findByUserAndCardFlashcard(User user, Flashcard flashcard);

    List<UserSeenCard> findByUserAndCardFlashcardAndStateOfKnowledgeIsFalse(User user, Flashcard flashcard);

    @Query("select u.card from UserSeenCard u " +
            "where " +
            "u.user = :user " +
            "and (:stateOfKnowledge is null or u.stateOfKnowledge = :stateOfKnowledge) " +
            "and (:difficultyLevel is null or u.difficultyLevel = :difficultyLevel)")
    List<Card> findByUser(User user, Boolean stateOfKnowledge, DifficultyLevel difficultyLevel);

    List<UserSeenCard> findByUserAndCardFlashcardAndStateOfKnowledgeIsTrue(User user, Flashcard flashcard);
}
