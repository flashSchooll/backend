package com.flashcard.repository;

import com.flashcard.model.Topic;
import com.flashcard.model.User;
import com.flashcard.model.UserQuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserQuizAnswerRepository extends JpaRepository<UserQuizAnswer, Long> {
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
}
