package com.flashcard.repository;

import com.flashcard.model.OwnerFlashcard;
import com.flashcard.model.OwnerCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OwnerCardRepository extends JpaRepository<OwnerCard,Long> {


    List<OwnerCard> findByOwnerFlashcardId(Long ownerFlashcardId);

    int countByOwnerFlashcard(OwnerFlashcard ownerFlashcard);
}
