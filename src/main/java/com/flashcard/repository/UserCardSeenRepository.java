package com.flashcard.repository;

import com.flashcard.model.TYTCard;
import com.flashcard.model.TYTFlashcard;
import com.flashcard.model.User;
import com.flashcard.model.UserSeenCard;
import com.flashcard.model.enums.DifficultyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCardSeenRepository extends JpaRepository<UserSeenCard, Long> {

    List<UserSeenCard> findByUserAndTytCardTytFlashcard(User user, TYTFlashcard flashcard);

    List<UserSeenCard> findByUserAndTytCardTytFlashcardAndStateOfKnowledgeIsFalse(User user, TYTFlashcard flashcard);

    @Query("select u.tytCard from UserSeenCard u " +
            "where " +
            "u.user = :user " +
            "and (:stateOfKnowledge is null or u.stateOfKnowledge = :stateOfKnowledge) " +
            "and (:difficultyLevel is null or u.difficultyLevel = :difficultyLevel)")
    List<TYTCard> findByUser(User user, Boolean stateOfKnowledge, DifficultyLevel difficultyLevel);

    List<UserSeenCard> findByUserAndTytCardTytFlashcardAndStateOfKnowledgeIsTrue(User user, TYTFlashcard flashcard);
}
