package com.flashcard.repository;

import com.flashcard.model.Podcast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PodcastRepository extends JpaRepository<Podcast, Long> {
    List<Podcast> findByTopicId(Long topicId);
}
