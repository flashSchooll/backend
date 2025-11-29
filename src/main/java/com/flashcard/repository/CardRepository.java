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

    @Query("SELECT c.frontPhotoPath FROM Card c")
    List<String> findAllFrontPath();

    @Query("SELECT c.backPhotoPath FROM Card c")
    List<String> findAllBackPath();

    int countByFlashcardTopic(Topic topic);
}
