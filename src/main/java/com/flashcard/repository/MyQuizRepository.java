package com.flashcard.repository;

import com.flashcard.model.MyQuiz;
import com.flashcard.model.Topic;
import com.flashcard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MyQuizRepository extends JpaRepository<MyQuiz, Long> {
    List<MyQuiz> findByUser(User user);

    List<MyQuiz> findByUserId(Long userId);

    @Query("SELECT mq FROM MyQuiz mq " +
            "JOIN FETCH mq.quiz q " +
            "WHERE mq.user = :user " +
            "AND q.topic = :topic")
    List<MyQuiz> findByUserAndQuizTopicWithFetch(@Param("user") User user,@Param("topic") Topic topic
    );
}
