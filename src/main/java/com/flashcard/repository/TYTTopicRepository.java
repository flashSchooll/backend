package com.flashcard.repository;

import com.flashcard.model.TYTLesson;
import com.flashcard.model.TYTTopic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TYTTopicRepository extends JpaRepository<TYTTopic,Long> {

    List<TYTTopic> findByTytLesson(TYTLesson tytLesson);

    Optional<TYTTopic> findBySubject(String subject);
}
