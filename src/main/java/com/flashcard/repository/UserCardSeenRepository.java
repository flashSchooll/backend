package com.flashcard.repository;

import com.flashcard.model.TYTFlashcard;
import com.flashcard.model.User;
import com.flashcard.model.UserSeenCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCardSeenRepository extends JpaRepository<UserSeenCard, Long> {

    List<UserSeenCard> findByUserAndFlashcard(User user, TYTFlashcard flashcard);

    List<UserSeenCard> findByUserAndFlashcardAndStateOfKnowledgeIsFalse(User user, TYTFlashcard flashcard);

    List<UserSeenCard> findByUserAndFlashcardAndStateOfKnowledgeIsTrue(User user, TYTFlashcard flashcard);
}
