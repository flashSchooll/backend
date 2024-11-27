package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.tyttopic.Request.TYTTopicSaveRequest;
import com.flashcard.controller.tyttopic.Request.TYTTopicUpdateRequest;
import com.flashcard.controller.tyttopic.Response.TYTTopicAdminResponse;
import com.flashcard.model.TYTLesson;
import com.flashcard.model.TYTTopic;
import com.flashcard.repository.TYTLessonRepository;
import com.flashcard.repository.TYTTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TYTTopicService {

    private final TYTTopicRepository tytTopicRepository;
    private final TYTLessonRepository tytLessonRepository;

    @Transactional
    public TYTTopicAdminResponse save(TYTTopicSaveRequest tytLessonSaveRequest) {
        Objects.requireNonNull(tytLessonSaveRequest.getLessonId());
        Objects.requireNonNull(tytLessonSaveRequest.getSubject());

        TYTLesson tytLesson = tytLessonRepository.findById(tytLessonSaveRequest.getLessonId())
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_LESSON_NOT_FOUND));

        TYTTopic topic = new TYTTopic();
        topic.setTytLesson(tytLesson);
        topic.setSubject(tytLessonSaveRequest.getSubject());

        topic = tytTopicRepository.save(topic);

        return new TYTTopicAdminResponse(topic);
    }

    @Transactional
    public TYTTopicAdminResponse update(TYTTopicUpdateRequest tytTopicUpdateRequest) {
        Objects.requireNonNull(tytTopicUpdateRequest.getTopicId());
        Objects.requireNonNull(tytTopicUpdateRequest.getSubject());

        TYTTopic topic = tytTopicRepository.findById(tytTopicUpdateRequest.getTopicId())
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_TOPIC_NOT_FOUND));

        topic.setSubject(tytTopicUpdateRequest.getSubject());

        topic = tytTopicRepository.save(topic);

        return new TYTTopicAdminResponse(topic);
    }

    @Transactional
    public void delete(Long id) {
        Objects.requireNonNull(id);

        TYTTopic topic = tytTopicRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_TOPIC_NOT_FOUND));

        tytTopicRepository.delete(topic);
    }

    public List<TYTTopicAdminResponse> getAll(Long lessonId) {
        Objects.requireNonNull(lessonId);

        TYTLesson tytLesson = tytLessonRepository.findById(lessonId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_LESSON_NOT_FOUND));

        return tytTopicRepository.findByTytLesson(tytLesson).stream().map(TYTTopicAdminResponse::new).toList();
    }
}
