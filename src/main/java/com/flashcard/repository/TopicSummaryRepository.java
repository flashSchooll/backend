package com.flashcard.repository;

import com.flashcard.model.Topic;
import com.flashcard.model.TopicSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicSummaryRepository extends JpaRepository<TopicSummary, Long> {

    Page<TopicSummary> findByTopic(Topic topic, Pageable pageable);

    List<TopicSummary> findByTopic(Topic topic);
}
