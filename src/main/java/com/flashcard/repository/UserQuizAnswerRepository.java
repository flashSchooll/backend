package com.flashcard.repository;

import com.flashcard.model.Topic;
import com.flashcard.model.User;
import com.flashcard.model.UserQuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserQuizAnswerRepository extends JpaRepository<UserQuizAnswer, Long>,
        JpaSpecificationExecutor<UserQuizAnswer> {
    List<UserQuizAnswer> findByUserAndQuizName(User user, String name);

    boolean existsByUserAndQuizName(User user, String name);

    boolean existsByUserIdAndQuizName(Long userId, String key);

    List<UserQuizAnswer> findByUserIdAndQuizNameAndQuizTopic(Long userId, String name, Topic topic);

    List<UserQuizAnswer> findByUserAndQuizTopic(User user, Topic topic);

    @Query("""
    select u
    from UserQuizAnswer u
    join u.quiz qu
    where u.user = :user
    and qu.id in (:quizIds)
""")
    List<UserQuizAnswer> findByUserAndQuizIdIn(
            @Param("user") User user,
            @Param("quizIds") List<Long> quizIds
    );

    void deleteByQuizTopicAndQuizName(Topic topic, String name);

    @Query("SELECT uqa FROM UserQuizAnswer uqa " +
            "JOIN FETCH uqa.quiz q " +
            "JOIN FETCH q.topic t " +
            "JOIN FETCH t.lesson l " +
            "WHERE uqa.user = :user " +
            "AND uqa.deleted = false " +
            "AND (uqa.createdDate >= :startDate) " +
            "AND (uqa.createdDate <= :endDate)")
    List<UserQuizAnswer> findByUserAndDateRange(
            @Param("user") User user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
