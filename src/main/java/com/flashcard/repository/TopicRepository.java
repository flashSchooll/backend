package com.flashcard.repository;

import com.flashcard.model.Lesson;
import com.flashcard.model.Topic;
import com.flashcard.model.enums.YKS;
import com.flashcard.model.enums.YKSLesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Page<Topic> findByLesson(Lesson tytLesson, Pageable pageable);

    List<Topic> findByLesson(Lesson tytLesson);

    Optional<Topic> findBySubjectAndLesson(String subject, Lesson lesson);

    @Query("""
                SELECT t FROM Topic t
                WHERE (:lessonId IS NULL OR t.lesson.id = :lessonId)
            
                AND (:yksLesson IS NULL OR t.lesson.yksLesson = :yksLesson)
            """)
    Page<Topic> getBySearch(
            @Param("lessonId") Long lessonId,
        //    @Param("query") String query,
            @Param("yksLesson") YKSLesson yksLesson,
            Pageable pageable);

    List<Topic> findByLessonYks(YKS yks);

    @Query("SELECT COALESCE(MAX(t.index), 0) FROM Topic t WHERE t.lesson = :lesson AND t.deleted = false")
    Integer findMaxIndexByLesson(@Param("lesson") Lesson lesson);
}
//  AND (:query IS NULL OR LOWER(t.subject) LIKE LOWER(CONCAT('%', :query, '%')))