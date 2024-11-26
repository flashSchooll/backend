package com.flashcard.repository;

import com.flashcard.model.TYTFlashcard;
import com.flashcard.model.TYTTopic;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface TYTFlashCardRepository extends JpaRepository<TYTFlashcard, Long> {

    List<TYTFlashcard> findByTopic(TYTTopic topic);

    boolean existsByCardName(String cardName);
}
