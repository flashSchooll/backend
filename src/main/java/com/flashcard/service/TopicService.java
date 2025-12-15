package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.topic.admin.request.TopicSaveRequest;
import com.flashcard.controller.topic.admin.request.TopicUpdateRequest;
import com.flashcard.controller.topic.user.response.TopicUserIdResponse;
import com.flashcard.controller.topic.user.response.TopicUserResponse;
import com.flashcard.model.Lesson;
import com.flashcard.model.Topic;
import com.flashcard.model.User;
import com.flashcard.model.enums.YKS;
import com.flashcard.model.enums.YKSLesson;
import com.flashcard.repository.CardRepository;
import com.flashcard.repository.LessonRepository;
import com.flashcard.repository.TopicRepository;
import com.flashcard.repository.UserSeenCardRepository;
import com.flashcard.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final LessonRepository lessonRepository;
    private final CardRepository cardRepository;
    private final UserSeenCardRepository userSeenCardRepository;
    private final AuthService authService;

    @Transactional
    public Topic save(TopicSaveRequest topicSaveRequest) {
        Objects.requireNonNull(topicSaveRequest.getLessonId());
        Objects.requireNonNull(topicSaveRequest.getSubject());

        Lesson tytLesson = lessonRepository.findById(topicSaveRequest.getLessonId())
                .orElseThrow(() -> new NoSuchElementException(Constants.LESSON_NOT_FOUND));

        Integer existingTopicCount = topicRepository.findMaxIndexByLesson(tytLesson);

        if (existingTopicCount == null) {
            existingTopicCount = 1;
        }

        Topic topic = new Topic();
        topic.setLesson(tytLesson);
        topic.setSubject(topicSaveRequest.getSubject());
        topic.setDeleted(false);
        topic.setIndex(existingTopicCount);

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

    public Page<Topic> getAllWithLesson(Long lessonId, Pageable pageable) {
        Objects.requireNonNull(lessonId);

        Lesson tytLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NoSuchElementException(Constants.LESSON_NOT_FOUND));

        return topicRepository.findByLesson(tytLesson, pageable);
    }

    // @Cacheable(value = "lessonTopic", key = "#lessonId")
    public List<TopicUserResponse> getAllByLesson(Long lessonId) {
        Objects.requireNonNull(lessonId);

        User user = authService.getCurrentUser();

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NoSuchElementException(Constants.LESSON_NOT_FOUND));

        // Konuları veritabanından sadece 1 kere çekiyoruz
        List<Topic> topics = topicRepository.findByLesson(lesson);

        return topics.stream()
                .map(topic -> {
                    Integer totalCount = cardRepository.countByFlashcardTopicAndFlashcardCanBePublishTrue(topic);
                    if (totalCount != null && totalCount > 0) {
                        Integer seenCount = userSeenCardRepository.countByUserAndCardFlashcardTopic(user, topic);
                        return new TopicUserResponse(topic, totalCount, seenCount);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public Page<Topic> getAll(Pageable pageable) {
        return topicRepository.findAll(pageable);
    }

    public Page<Topic> getBySearch(Long lessonId, YKSLesson yksLesson, Pageable pageable) {
        String yksLessonStr = (yksLesson != null) ? yksLesson.toString() : null;
        return topicRepository.getBySearch(lessonId, yksLesson, pageable);
    }

    public Topic getById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));
    }

    public List<TopicUserIdResponse> getAllByYks(YKS yks) {
        return topicRepository.findByLessonYks(yks)
                .stream()
                .map(TopicUserIdResponse::new)
                .toList();
    }
}
