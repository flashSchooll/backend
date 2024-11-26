package com.flashcard.repository;

import com.flashcard.model.TYTLesson;
import com.flashcard.model.TYTTopic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface TYTTopicRepository extends JpaRepository<TYTTopic,Long> {

    List<TYTTopic> findByTytLesson(TYTLesson tytLesson);
}
