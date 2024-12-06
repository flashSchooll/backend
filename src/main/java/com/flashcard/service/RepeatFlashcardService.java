package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.model.RepeatFlashcard;
import com.flashcard.model.Flashcard;
import com.flashcard.model.Topic;
import com.flashcard.model.User;
import com.flashcard.repository.RepeatFlashcardRepository;
import com.flashcard.repository.FlashCardRepository;
import com.flashcard.repository.TopicRepository;
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
    private final FlashCardRepository flashCardRepository;
    private final TopicRepository topicRepository;

    @Transactional
    public RepeatFlashcard save(Long flashcardId, LocalDateTime repeatTime) {
        Objects.requireNonNull(flashcardId);

        User user = authService.getCurrentUser();
        Flashcard flashcard = flashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));

        RepeatFlashcard repeatFlashcard = new RepeatFlashcard();
        repeatFlashcard.setUser(user);
        repeatFlashcard.getFlashcards().add(flashcard);
        repeatFlashcard.setTopic(flashcard.getTopic().getSubject());
        repeatFlashcard.setLesson(flashcard.getTopic().getLesson().getYksLesson().label);
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

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_TOPIC_NOT_FOUND));

        User user = authService.getCurrentUser();

        List<Flashcard> flashcards = flashCardRepository.findByTopic(topic);

        RepeatFlashcard repeatFlashcard = new RepeatFlashcard();
        repeatFlashcard.setUser(user);
        repeatFlashcard.setFlashcards(flashcards);
        repeatFlashcard.setTopic(topic.getSubject());
        repeatFlashcard.setLesson(topic.getLesson().getYksLesson().name());
        repeatFlashcard.setTopicId(topicId);
        repeatFlashcard.setRepeatTime(repeatTime);

        return repeatFlashcardRepository.save(repeatFlashcard);
    }
}
