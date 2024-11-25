package com.flashcard.repository;

import com.flashcard.model.TYTTopic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TYTTopicRepository extends JpaRepository<TYTTopic,Long> {
}
