package com.flashcard.repository;

import com.flashcard.model.Lesson;
import com.flashcard.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    List<Topic> findByLesson(Lesson tytLesson);

    Optional<Topic> findBySubjectAndLesson(String subject, Lesson lesson);
}
