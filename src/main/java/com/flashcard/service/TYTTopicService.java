package com.flashcard.service;

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
    public void save(TYTTopicSaveRequest tytLessonSaveRequest) {
        Objects.requireNonNull(tytLessonSaveRequest.getTytLessonId());
        Objects.requireNonNull(tytLessonSaveRequest.getSubject());

        TYTLesson tytLesson = tytLessonRepository.findById(tytLessonSaveRequest.getTytLessonId())
                .orElseThrow(() -> new NoSuchElementException("Ders bulunamadı"));

        TYTTopic topic = new TYTTopic();
        topic.setTytLesson(tytLesson);
        topic.setSubject(tytLessonSaveRequest.getSubject());

        tytTopicRepository.save(topic);
    }

    @Transactional
    public void update(TYTTopicUpdateRequest tytTopicUpdateRequest) {
        Objects.requireNonNull(tytTopicUpdateRequest.getTopic_id());
        Objects.requireNonNull(tytTopicUpdateRequest.getSubject());

        TYTTopic topic = tytTopicRepository.findById(tytTopicUpdateRequest.getTopic_id())
                .orElseThrow(() -> new NoSuchElementException("Konu bulunamadı"));

        topic.setSubject(tytTopicUpdateRequest.getSubject());

        tytTopicRepository.save(topic);
    }

    @Transactional
    public void delete(Long id) {
        Objects.requireNonNull(id);

        TYTTopic topic = tytTopicRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Konu bulunamadı"));

        tytTopicRepository.delete(topic);
    }

    public List<TYTTopicAdminResponse> getAll() {
        return tytTopicRepository.findAll().stream().map(TYTTopicAdminResponse::new).toList();
    }
}
