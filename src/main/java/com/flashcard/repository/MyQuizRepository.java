package com.flashcard.repository;

import com.flashcard.model.MyQuiz;
import com.flashcard.model.Topic;
import com.flashcard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MyQuizRepository extends JpaRepository<MyQuiz, Long> {
    List<MyQuiz> findByUser(User user);

    @Query("SELECT mq FROM MyQuiz mq " +
            "JOIN FETCH mq.user u " +
            "JOIN FETCH mq.quiz q " +
            "WHERE u.id = :userId")
    List<MyQuiz> findByUserId(@Param("userId") Long userId);


    @Query("SELECT mq FROM MyQuiz mq " +
            "JOIN FETCH mq.quiz q " +
            "WHERE mq.user = :user " +
            "AND q.topic = :topic")
    List<MyQuiz> findByUserAndQuizTopicWithFetch(@Param("user") User user, @Param("topic") Topic topic
    );

    @Query("SELECT mq FROM MyQuiz mq " +
            "JOIN FETCH mq.quiz q " +
            "WHERE mq.user = :user " +
            "AND q.name = :name")
    List<MyQuiz> findByUserAndQuizNameWithFetch(User user, String name);

    Optional<MyQuiz> findByQuizId(Long myQuizId);
}
