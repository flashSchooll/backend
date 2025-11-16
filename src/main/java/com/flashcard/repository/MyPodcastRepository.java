package com.flashcard.repository;

import com.flashcard.model.MyPodcast;
import com.flashcard.model.Podcast;
import com.flashcard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MyPodcastRepository extends JpaRepository<MyPodcast, Long> {
    List<MyPodcast> findByUserAndPodcastTopicId(User user, Long topicId);

    Optional<MyPodcast> findByUserAndPodcastId(User user, Long podcastId);

    @Query("select p.podcast from MyPodcast p where p.user = :user")
    List<Podcast> findByUser(User user);
}
