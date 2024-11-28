package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.tyttopic.admin.Request.TYTTopicSaveRequest;
import com.flashcard.controller.tyttopic.admin.Request.TYTTopicUpdateRequest;
import com.flashcard.controller.tyttopic.admin.Response.TYTTopicAdminResponse;
import com.flashcard.controller.tyttopic.user.response.TYTTopicUserResponse;
import com.flashcard.model.TYTCard;
import com.flashcard.model.TYTLesson;
import com.flashcard.model.TYTTopic;
import com.flashcard.repository.TYTCardRepository;
import com.flashcard.repository.TYTLessonRepository;
import com.flashcard.repository.TYTTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TYTTopicService {

    private final TYTTopicRepository tytTopicRepository;
    private final TYTLessonRepository tytLessonRepository;
    private final TYTCardRepository cardRepository;

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

    public List<TYTTopicUserResponse> getAllUser(Long lessonId) {
        Objects.requireNonNull(lessonId);

        TYTLesson tytLesson = tytLessonRepository.findById(lessonId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_LESSON_NOT_FOUND));

        List<TYTCard> tytCards = cardRepository.findByTYTLesson(tytLesson);

        Map<TYTTopic, Long> cardCount = tytCards.stream()
                .collect(Collectors.groupingBy(
                        tytCard -> tytCard.getTytFlashcard().getTopic(),
                        Collectors.counting()
                ));

        return tytTopicRepository.findByTytLesson(tytLesson)
                .stream()
                .map(topic->new TYTTopicUserResponse(topic, Math.toIntExact(cardCount.get(topic))))
                .toList();
    }
}
