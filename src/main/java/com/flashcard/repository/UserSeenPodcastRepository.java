package com.flashcard.repository;

import com.flashcard.model.MyPodcast;
import com.flashcard.model.Podcast;
import com.flashcard.model.User;
import com.flashcard.model.UserSeenPodcast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserSeenPodcastRepository extends JpaRepository<UserSeenPodcast, String> {
    Optional<UserSeenPodcast> findByPodcastIdAndUser(Long podcastId, User user);

    List<UserSeenPodcast> findByUser(User user);

    boolean existsByUserAndPodcastId(User currentUser, Long id);

    @Query("SELECT usp.podcast.id FROM UserSeenPodcast usp " +
            "WHERE usp.user = :user AND usp.podcast.topic.id = :topicId")
    List<Long> findByUserAndPodcastTopicId(User user, Long topicId);

    @Query("SELECT usp.podcast.id FROM UserSeenPodcast usp WHERE usp.user = :user ")
    List<Long> findIdsByUser(User user);


}
