package com.flashcard.repository;

import com.flashcard.model.OwnerFlashcard;
import com.flashcard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OwnerFlashcardRepository extends JpaRepository<OwnerFlashcard,Long> {

    List<OwnerFlashcard> findByUser(User user);
}
