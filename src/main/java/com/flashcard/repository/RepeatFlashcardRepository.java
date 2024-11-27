package com.flashcard.repository;

import com.flashcard.model.RepeatFlashcard;
import com.flashcard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepeatFlashcardRepository extends JpaRepository<RepeatFlashcard,Long> {

    List<RepeatFlashcard> findByUser(User user);
}
