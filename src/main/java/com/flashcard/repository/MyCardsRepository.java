package com.flashcard.repository;

import com.flashcard.model.MyCard;
import com.flashcard.model.TYTCard;
import com.flashcard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MyCardsRepository extends JpaRepository<MyCard, Long> {
    List<MyCard> findByUser(User user);

    Optional<MyCard> findByUserAndTytCard(User user, TYTCard tytCard);
}
