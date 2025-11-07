package com.flashcard.repository;

import com.flashcard.model.*;
import com.flashcard.model.enums.YKS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserSeenCardRepository extends JpaRepository<UserSeenCard, Long> {

    List<UserSeenCard> findByUserAndCardFlashcard(User user, Flashcard flashcard);

    List<UserSeenCard> findByUserAndCardFlashcardAndStateOfKnowledgeIsFalse(User user, Flashcard flashcard);

    @Query("select u.card from UserSeenCard u " +
            "where " +
            "u.user = :user " +
            "and (:stateOfKnowledge is null or u.stateOfKnowledge = :stateOfKnowledge)")
    List<Card> findByUser(User user, Boolean stateOfKnowledge);

    List<UserSeenCard> findByUserAndCardFlashcardAndStateOfKnowledgeIsTrue(User user, Flashcard flashcard);

    @Query("SELECT usc FROM UserSeenCard usc " +
            "JOIN FETCH usc.card c " +
            "JOIN FETCH c.flashcard f " +
            "JOIN FETCH f.topic t " +
            "WHERE usc.user = :user AND t = :topic")
    List<UserSeenCard> findByUserAndCardFlashcardTopic(@Param("user") User user, @Param("topic") Topic topic);


    int countByUser(User user);

    List<UserSeenCard> findByUser(User user);

    Integer countByUserAndCardFlashcard(User user, Flashcard flashcard);

    @Query("SELECT f.id FROM UserSeenCard rf " +
            "LEFT JOIN rf.card c " +
            "LEFT JOIN c.flashcard f " +
            "WHERE rf.user = :user")
    List<Long> findByUserWithAllData(User user);

    @Query("SELECT distinct f.id FROM UserSeenCard usc " +
            "JOIN usc.card c " +
            "JOIN c.flashcard f " +
            "JOIN f.topic t " +
            "WHERE usc.user = :user AND t.id = :topicId")
    List<Long> findByUserAndCardFlashcardTopic(@Param("user") User user, @Param("topicId") Long topicId);


    int countByUserAndCardFlashcardTopicLessonYks(User user, YKS yks);
}
