package com.flashcard.repository;

import com.flashcard.model.Topic;
import com.flashcard.model.TopicSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicSummaryRepository extends JpaRepository<TopicSummary,Long> {

    List<TopicSummary> findByTopic(Topic topic);
}
