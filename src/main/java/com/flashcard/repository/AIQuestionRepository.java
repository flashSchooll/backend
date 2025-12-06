package com.flashcard.repository;

import com.flashcard.model.AIQuestion;
import com.flashcard.model.Topic;
import com.flashcard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AIQuestionRepository extends JpaRepository<AIQuestion, String> {
    @Query("""
            SELECT aq FROM AIQuestion aq
            WHERE aq.published = true
            """)
    List<AIQuestion> findAllPublished();

    List<AIQuestion> findByTopicAndPublishedTrueAndUserNot(Topic topic, User user);

    List<AIQuestion> findByTopic(Topic topic);

    List<AIQuestion> findAllByPublishedTrueAndUserNot(User user);

    List<AIQuestion> findByUser( User user);
}
