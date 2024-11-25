package com.flashcard.repository;

import com.flashcard.model.TYTCard;
import com.flashcard.model.TYTFlashcard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TYTCardRepository extends JpaRepository<TYTCard, Long> {

    List<TYTCard> findByTytFlashcard(TYTFlashcard flashcard);

    int countByTytFlashcard(TYTFlashcard flashcard);
}
