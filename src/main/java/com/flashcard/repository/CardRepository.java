package com.flashcard.repository;

import com.flashcard.model.*;
import com.flashcard.model.enums.Branch;
import com.flashcard.model.enums.YKS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {


    List<Card> findByFlashcard(Flashcard flashcard);

    @Query("select c from Card c where c.flashcard = :flashcard")
    @EntityGraph("card-graph")
    Page<Card> findCardsWithFlashcard(Flashcard flashcard, Pageable pageable);

    @Query("select c from Card c where c.flashcard = :flashcard")
    @EntityGraph("card-graph")
    List<Card> findCardsWithFlashcard(Flashcard flashcard);


    int countByFlashcard(Flashcard flashcard);

    @Query("SELECT COUNT(c) FROM Card c WHERE c.flashcard.topic.lesson = :lesson and c.flashcard.canBePublish = true")
    int countByFlashcardTopicLesson(@Param("lesson") Lesson lesson);

    @Query("SELECT c FROM Card c WHERE c.flashcard.topic.lesson = :lesson")
    List<Card> findByLesson(Lesson lesson);

    @Query("SELECT c FROM Card c WHERE c.flashcard.topic = :topic")
    List<Card> findByTopic(Topic topic);


    int countByFlashcardTopicLessonYksAndFlashcardCanBePublishTrue(YKS yks);


    //  @Query(value = """
    //          WITH filtered_lessons AS (
    //            SELECT id
    //            FROM lesson
    //            WHERE branch = :branch OR :branch IS NULL OR branch IS NULL
    //          ),
    //          total_lessons AS (
    //            SELECT COUNT(*) as total FROM filtered_lessons
    //          ),
    //          randomized_cards AS (
    //            SELECT
    //              c.*,
    //              CEIL(100.0 / (SELECT total FROM total_lessons)) as per_lesson_limit,
    //              ROW_NUMBER() OVER (
    //                PARTITION BY t.lesson_id
    //                ORDER BY RANDOM()
    //              ) as lesson_rn
    //            FROM card c
    //            INNER JOIN flashcard f ON c.flashcard_id = f.id
    //            INNER JOIN topic t ON f.topic_id = t.id
    //            INNER JOIN filtered_lessons l ON t.lesson_id = l.id
    //          )
    //          SELECT *
    //          FROM randomized_cards
    //          WHERE lesson_rn <= per_lesson_limit
    //          ORDER BY RANDOM()
    //          LIMIT 100
    //          """, nativeQuery = true)
    @Query("SELECT  c FROM Card c " +
            "JOIN FETCH c.flashcard f " +       // Flashcard'ı hemen çek
            "JOIN FETCH f.topic t " +           // Topic'i hemen çek
            "JOIN FETCH t.lesson l " +          // Lesson'ı hemen çek
            "WHERE l.branch IS NULL OR l.branch = :branch " +
            "AND f.canBePublish = true " +
            "ORDER BY RANDOM() " +
            "LIMIT 70")
    List<Card> findRandomCardsByBranch(@Param("branch") Branch branch);//todo düzgün çalışmıyor

    @Query("SELECT DISTINCT c FROM Card c JOIN FETCH c.flashcard f WHERE f.id IN :flashcardIds")
    List<Card> findByFlashcardIn(List<Long> flashcardIds);

    Long countByFlashcardTopicAndFlashcardCanBePublishTrue(Topic topic);

    int countByFlashcardTopicLessonYksAndFlashcardTopicLessonBranchAndFlashcardCanBePublishTrue(YKS yks, Branch branch);

    @Query("SELECT c FROM Card c " +
            "JOIN FETCH c.flashcard f " +
            "JOIN FETCH f.topic t " +
            "JOIN FETCH t.lesson l " +
            "WHERE l.yks = :yks " +
            "AND f.canBePublish = true")
    List<Card> findByFlashcardTopicLessonYks(@Param("yks") YKS yks);

    @Query("SELECT c FROM Card c " +
            "JOIN FETCH c.flashcard f " +
            "JOIN FETCH f.topic t " +
            "JOIN FETCH t.lesson l " +
            "WHERE l.yks = :yks " +
            "AND l.branch = :branch " +
            "AND f.canBePublish = true")
    List<Card> findByFlashcardTopicLessonYksBranch(YKS yks, Branch branch);

 //  @Query("""
 //              SELECT c FROM Card c
 //              WHERE c IN (SELECT mc.card FROM MyCard mc WHERE mc.user = :user)
 //              OR c IN (SELECT usc.card FROM UserSeenCard usc WHERE usc.user = :user)
 //              ORDER BY RANDOM()
 //              LIMIT 100
 //          """)
 //  List<Card> findCombinedCards(User user);


    @Query(value = """
                    SELECT c.*
                    FROM card c
                    WHERE c.id IN (
                        SELECT mc.card_id
                        FROM my_card mc
                        WHERE mc.user_id = :userId
            
                        UNION
            
                        SELECT c2.id
                        FROM repeat_flashcard rf
                        JOIN repeat_flashcard_flashcards rff ON rf.id = rff.repeat_flashcard_id
                        JOIN flashcard f ON rff.flashcards_id = f.id
                        JOIN card c2 ON c2.flashcard_id = f.id
                        WHERE rf.user_id = :userId
                    )
                    ORDER BY RANDOM()
                    LIMIT 50
            """, nativeQuery = true)
    List<Card> getUserRepeatCardsAndMyCards(@Param("userId") Long userId); // todo test edilecek
}
