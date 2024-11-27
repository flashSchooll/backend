package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.model.RepeatFlashcard;
import com.flashcard.model.TYTFlashcard;
import com.flashcard.model.TYTTopic;
import com.flashcard.model.User;
import com.flashcard.repository.RepeatFlashcardRepository;
import com.flashcard.repository.TYTFlashCardRepository;
import com.flashcard.repository.TYTTopicRepository;
import com.flashcard.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class RepeatFlashcardService {

    private final RepeatFlashcardRepository repeatFlashcardRepository;
    private final AuthService authService;
    private final TYTFlashCardRepository tytFlashCardRepository;
    private final TYTTopicRepository tytTopicRepository;

    @Transactional
    public RepeatFlashcard save(Long flashcardId, LocalDateTime repeatTime) {
        Objects.requireNonNull(flashcardId);

        User user = authService.getCurrentUser();
        TYTFlashcard flashcard = tytFlashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));

        RepeatFlashcard repeatFlashcard = new RepeatFlashcard();
        repeatFlashcard.setUser(user);
        repeatFlashcard.getTytFlashcard().add(flashcard);
        repeatFlashcard.setTopic(flashcard.getTopic().getSubject());
        repeatFlashcard.setLesson(flashcard.getTopic().getTytLesson().getTyt().label);
        repeatFlashcard.setTopicId(flashcard.getTopic().getId());
        repeatFlashcard.setRepeatTime(repeatTime);

        return repeatFlashcardRepository.save(repeatFlashcard);
    }

    @Transactional
    public void delete(Long id) {
        Objects.requireNonNull(id);

        RepeatFlashcard repeatFlashcard = repeatFlashcardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.REPEAT_CARD_NOT_FOUND));

        repeatFlashcardRepository.delete(repeatFlashcard);
    }

    public List<RepeatFlashcard> getAll() {
        User user = authService.getCurrentUser();

        return repeatFlashcardRepository.findByUser(user);
    }

    public RepeatFlashcard saveByTopic(Long topicId, LocalDateTime repeatTime) {
        Objects.requireNonNull(topicId);

        TYTTopic topic = tytTopicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_TOPIC_NOT_FOUND));

        User user = authService.getCurrentUser();

        List<TYTFlashcard> flashcards = tytFlashCardRepository.findByTopic(topic);

        RepeatFlashcard repeatFlashcard = new RepeatFlashcard();
        repeatFlashcard.setUser(user);
        repeatFlashcard.setTytFlashcard(flashcards);
        repeatFlashcard.setTopic(topic.getSubject());
        repeatFlashcard.setLesson(topic.getTytLesson().getTyt().label);
        repeatFlashcard.setTopicId(topicId);
        repeatFlashcard.setRepeatTime(repeatTime);

        return repeatFlashcardRepository.save(repeatFlashcard);
    }
}
