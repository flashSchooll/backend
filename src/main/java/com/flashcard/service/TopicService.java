package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.topic.admin.Request.TopicSaveRequest;
import com.flashcard.controller.topic.admin.Request.TopicUpdateRequest;
import com.flashcard.controller.topic.user.response.TopicUserResponse;
import com.flashcard.model.Card;
import com.flashcard.model.Lesson;
import com.flashcard.model.Topic;
import com.flashcard.repository.CardRepository;
import com.flashcard.repository.LessonRepository;
import com.flashcard.repository.TopicRepository;
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
public class TopicService {

    private final TopicRepository topicRepository;
    private final LessonRepository lessonRepository;
    private final CardRepository cardRepository;

    @Transactional
    public Topic save(TopicSaveRequest topicSaveRequest) {
        Objects.requireNonNull(topicSaveRequest.getLessonId());
        Objects.requireNonNull(topicSaveRequest.getSubject());

        Lesson tytLesson = lessonRepository.findById(topicSaveRequest.getLessonId())
                .orElseThrow(() -> new NoSuchElementException(Constants.LESSON_NOT_FOUND));

        Topic topic = new Topic();
        topic.setLesson(tytLesson);
        topic.setSubject(topicSaveRequest.getSubject());

        return topicRepository.save(topic);
    }

    @Transactional
    public Topic update(TopicUpdateRequest topicUpdateRequest) {
        Objects.requireNonNull(topicUpdateRequest.getTopicId());
        Objects.requireNonNull(topicUpdateRequest.getSubject());

        Topic topic = topicRepository.findById(topicUpdateRequest.getTopicId())
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        topic.setSubject(topicUpdateRequest.getSubject());

        return topicRepository.save(topic);
    }

    @Transactional
    public void delete(Long id) {
        Objects.requireNonNull(id);

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        topicRepository.delete(topic);
    }

    public List<Topic> getAll(Long lessonId) {
        Objects.requireNonNull(lessonId);

        Lesson tytLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NoSuchElementException(Constants.LESSON_NOT_FOUND));

        return topicRepository.findByLesson(tytLesson);
    }

    public List<TopicUserResponse> getAllUser(Long lessonId) {
        Objects.requireNonNull(lessonId);

        Lesson tytLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NoSuchElementException(Constants.LESSON_NOT_FOUND));

        List<Card> tytCards = cardRepository.findByLesson(tytLesson);

        Map<Topic, Long> cardCount = tytCards.stream()
                .collect(Collectors.groupingBy(
                        tytCard -> tytCard.getFlashcard().getTopic(),
                        Collectors.counting()
                ));

        return topicRepository.findByLesson(tytLesson)
                .stream()
                .map(topic -> new TopicUserResponse(topic, Math.toIntExact(cardCount.get(topic))))
                .toList();
    }
}
