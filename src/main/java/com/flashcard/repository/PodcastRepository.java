package com.flashcard.repository;

import com.flashcard.model.Podcast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PodcastRepository extends JpaRepository<Podcast, Long> {
    List<Podcast> findByTopicId(Long topicId);

    List<Podcast> findByTopicIdAndPublishedTrue(Long topicId);

    Optional<Podcast> findByIdAndPublishedTrue(Long podcastId);
}
