package com.flashcard.repository;

import com.flashcard.model.*;
import com.flashcard.model.enums.Branch;
import com.flashcard.model.enums.YKS;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {


    List<Card> findByFlashcard(Flashcard flashcard);

    @Query("select c from Card c where c.flashcard = :flashcard")
    @EntityGraph("card-graph")
    List<Card> findCardsWithFlashcard(Flashcard flashcard);


    int countByFlashcard(Flashcard flashcard);

    @Query("SELECT COUNT(c) FROM Card c WHERE c.flashcard.topic.lesson = :lesson")
    int countByFlashcardTopicLesson(@Param("lesson") Lesson lesson);

    @Query("SELECT c FROM Card c WHERE c.flashcard.topic.lesson = :lesson")
    List<Card> findByLesson(Lesson lesson);

    @Query("SELECT c FROM Card c WHERE c.flashcard.topic = :topic")
    List<Card> findByTopic(Topic topic);


    int countByFlashcardTopicLessonYks(YKS yks);


    @Query(value = """
                    WITH card_count AS (
                        SELECT COUNT(*) as total
                        FROM card c
                        INNER JOIN flashcard f ON c.flashcard_id = f.id
                        INNER JOIN topic t ON f.topic_id = t.id
                        INNER JOIN lesson l ON t.lesson_id = l.id
                        WHERE l.branch = :branch
                            OR l.branch IS NULL
                            OR :branch IS NULL
                    )
            SELECT c.*
                    FROM card c
                    INNER JOIN flashcard f ON c.flashcard_id = f.id
                    INNER JOIN topic t ON f.topic_id = t.id
                    INNER JOIN lesson l ON t.lesson_id = l.id
                    WHERE l.branch = :branch
                        OR l.branch IS NULL
                        OR :branch IS NULL
            OFFSET floor(random() * (
                        CASE
                            WHEN (SELECT total FROM card_count) > 100
                            THEN (SELECT total - 100 FROM card_count)
                            ELSE 0
                        END
                    ))
                    LIMIT 100
            """, nativeQuery = true)
    List<Card> findRandomCardsByBranch(@Param("branch") String branch);

    @Query("SELECT DISTINCT c FROM Card c JOIN FETCH c.flashcard f WHERE f.id IN :flashcardIds")
    List<Card> findByFlashcardIn(List<Long> flashcardIds);

    Long countByFlashcardTopic(Topic topic);

    int countByFlashcardTopicLessonYksAndFlashcardTopicLessonBranch(YKS yks, Branch branch);

    @Query("SELECT c FROM Card c " +
            "JOIN FETCH c.flashcard f " +
            "JOIN FETCH f.topic t " +
            "JOIN FETCH t.lesson l " +
            "WHERE l.yks = :yks")
    List<Card> findByFlashcardTopicLessonYks(@Param("yks") YKS yks);

    @Query("SELECT c FROM Card c " +
            "JOIN FETCH c.flashcard f " +
            "JOIN FETCH f.topic t " +
            "JOIN FETCH t.lesson l " +
            "WHERE l.yks = :yks " +
            "and l.branch = :branch")
    List<Card> findByFlashcardTopicLessonYksBranch(YKS yks, Branch branch);

    @Query("""
                SELECT c FROM Card c
                WHERE c IN (SELECT mc.card FROM MyCard mc WHERE mc.user = :user)
                OR c IN (SELECT usc.card FROM UserSeenCard usc WHERE usc.user = :user)
                ORDER BY RANDOM()
                LIMIT 100
            """)
    List<Card> findCombinedCards(User user);


    @Query(value = """
                    SELECT c.*
                    FROM my_card mc
                    JOIN card c ON mc.card_id = c.id
                    LEFT JOIN repeat_flashcard rf ON mc.user_id = rf.user_id
                    LEFT JOIN topic t ON rf.topic_id = t.id
                    LEFT JOIN flashcard f ON f.topic_id = t.id
                    LEFT JOIN repeat_flashcard_flashcards rff ON rf.id = rff.repeat_flashcard_id
                    WHERE mc.user_id = :userId
                       OR (rf.user_id = :userId AND rf.topic_id IS NOT NULL)
                       OR (rff.flashcards_id IS NOT NULL)
                    ORDER BY RANDOM()
                    LIMIT 100;
            
            """, nativeQuery = true)
    List<Card> getUserRepeatCardsAndMyCards(Long userId);
}
