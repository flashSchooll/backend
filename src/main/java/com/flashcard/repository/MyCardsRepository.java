package com.flashcard.repository;

import com.flashcard.model.MyCard;
import com.flashcard.model.Card;
import com.flashcard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MyCardsRepository extends JpaRepository<MyCard, Long> {
    List<MyCard> findByUser(User user);

    Optional<MyCard> findByUserAndCard(User user, Card Card);
}
