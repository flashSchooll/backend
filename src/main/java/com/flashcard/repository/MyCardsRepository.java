package com.flashcard.repository;

import com.flashcard.model.Flashcard;
import com.flashcard.model.MyCard;
import com.flashcard.model.Card;
import com.flashcard.model.User;
import com.flashcard.model.enums.DifficultyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MyCardsRepository extends JpaRepository<MyCard, Long> {
    @Query("select m from MyCard m " +
            "where m.user = :user " +
            "and (:difficultyLevel is null or m.difficultyLevel = :difficultyLevel)")
    List<MyCard> findByUser(User user, DifficultyLevel difficultyLevel);

    List<MyCard> findByUser(User user);

    Optional<MyCard> findByUserAndCard(User user, Card card);

    List<MyCard> findByUserAndCardFlashcard(User user, Flashcard flashcard);

    boolean existsByUserAndCard(User user, Card card);

    List<MyCard> findByUserAndCardIn(User user, List<Card> cards);
}
