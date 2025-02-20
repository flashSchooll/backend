package com.flashcard.repository;

import com.flashcard.model.OwnerFlashcard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerFlashcardRepository extends JpaRepository<OwnerFlashcard,Long> {
}
