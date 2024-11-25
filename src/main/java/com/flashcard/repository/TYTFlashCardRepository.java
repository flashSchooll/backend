package com.flashcard.repository;

import com.flashcard.model.TYTFlashcard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TYTFlashCardRepository extends JpaRepository<TYTFlashcard,Long> {
}
