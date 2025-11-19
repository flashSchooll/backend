package com.flashcard.repository;

import com.flashcard.model.RepeatTopic;
import com.flashcard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepeatTopicRepository extends JpaRepository<RepeatTopic, String> {
    boolean existsByUserAndTopicId(User user, Long topicId);

    RepeatTopic findByUserAndTopicId(User user, Long topicId);

    List<RepeatTopic> findByUser(User user);
}
