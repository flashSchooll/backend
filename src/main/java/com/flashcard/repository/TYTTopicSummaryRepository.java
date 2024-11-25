package com.flashcard.repository;

import com.flashcard.model.TYTTopic;
import com.flashcard.model.TYTTopicSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TYTTopicSummaryRepository extends JpaRepository<TYTTopicSummary,Long> {

    List<TYTTopicSummary> findByTopic(TYTTopic topic);
}
