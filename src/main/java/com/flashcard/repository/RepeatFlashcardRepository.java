package com.flashcard.repository;

import com.flashcard.model.RepeatFlashcard;
import com.flashcard.model.Topic;
import com.flashcard.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepeatFlashcardRepository extends JpaRepository<RepeatFlashcard, Long> {

    List<RepeatFlashcard> findByUser(User user);

    Optional<RepeatFlashcard> findByUserAndTopic(@NotNull User user, Topic topic);
}
