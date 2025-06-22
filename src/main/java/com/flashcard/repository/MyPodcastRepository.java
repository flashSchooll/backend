package com.flashcard.repository;

import com.flashcard.model.MyPodcast;
import com.flashcard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyPodcastRepository extends JpaRepository<MyPodcast, Long> {
    List<MyPodcast> findByUserAndPodcastTopicId(User user, Long topicId);
}
