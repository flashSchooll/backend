package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.repeatflashcard.response.RepeatFlashcardResponse;
import com.flashcard.model.Flashcard;
import com.flashcard.model.RepeatFlashcard;
import com.flashcard.model.Topic;
import com.flashcard.model.User;
import com.flashcard.repository.FlashCardRepository;
import com.flashcard.repository.RepeatFlashcardRepository;
import com.flashcard.repository.TopicRepository;
import com.flashcard.repository.UserSeenCardRepository;
import com.flashcard.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class RepeatFlashcardService {

    private final RepeatFlashcardRepository repeatFlashcardRepository;
    private final AuthService authService;
    private final FlashCardRepository flashCardRepository;
    private final TopicRepository topicRepository;
    private final UserSeenCardRepository userSeenCardRepository;

    @Transactional
    public RepeatFlashcard save(Long flashcardId, LocalDateTime repeatTime) {
        Objects.requireNonNull(flashcardId);

        User user = authService.getCurrentUser();
        Flashcard flashcard = flashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));

        Optional<RepeatFlashcard> optionalRepeatFlashcard = repeatFlashcardRepository.findByUserAndTopic(user, flashcard.getTopic());

        RepeatFlashcard repeatFlashcard;

        if (optionalRepeatFlashcard.isPresent()) {
            repeatFlashcard = optionalRepeatFlashcard.get();

            List<Long> flashcardIds = repeatFlashcard.getFlashcards().stream().map(Flashcard::getId).toList();

            if (!flashcardIds.contains(flashcard.getId())) {
                List<Flashcard> flashcards = repeatFlashcard.getFlashcards();
                flashcards.add(flashcard);
                repeatFlashcard.setFlashcards(flashcards);
            }

        } else {
            repeatFlashcard = new RepeatFlashcard();
            repeatFlashcard.setUser(user);
            repeatFlashcard.setFlashcards(List.of(flashcard));
            repeatFlashcard.setTopic(flashcard.getTopic());
            repeatFlashcard.setRepeatTime(repeatTime);
        }

        return repeatFlashcardRepository.save(repeatFlashcard);
    }

    @Transactional
    public void delete(Long id) {
        Objects.requireNonNull(id);

        RepeatFlashcard repeatFlashcard = repeatFlashcardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.REPEAT_CARD_NOT_FOUND));

        repeatFlashcardRepository.delete(repeatFlashcard);
    }

    public List<RepeatFlashcardResponse> getAll() {// todo buraya bakılacak
        User user = authService.getCurrentUser();

        List<RepeatFlashcard> repeatFlashcards = repeatFlashcardRepository.findByUserWithTopicAndLesson(user);

        List<Long> ids=userSeenCardRepository.findByUserWithAllData(user);

        return repeatFlashcards
                .stream()
                .map(
                        repeatFlashcard -> new RepeatFlashcardResponse(repeatFlashcard, ids))
                .toList();
    }

    @Transactional
    public RepeatFlashcard saveByTopic(Long topicId, LocalDateTime repeatTime) {
        Objects.requireNonNull(topicId);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        User user = authService.getCurrentUser();
        Optional<RepeatFlashcard> optionalRepeatFlashcard = repeatFlashcardRepository.findByUserAndTopic(user, topic);
        List<Flashcard> flashcards = flashCardRepository.findByTopicAndCanBePublishTrue(topic);
        Set<Flashcard> flashcardSet = new HashSet<>(flashcards);

        RepeatFlashcard repeatFlashcard = optionalRepeatFlashcard
                .map(existingRepeatFlashcard -> {
                    existingRepeatFlashcard.setFlashcards(new ArrayList<>(flashcardSet)); // Düzeltme burada
                    return existingRepeatFlashcard;
                })
                .orElseGet(() -> {
                    RepeatFlashcard newRepeatFlashcard = new RepeatFlashcard();
                    newRepeatFlashcard.setUser(user);
                    newRepeatFlashcard.setFlashcards(new ArrayList<>(flashcardSet)); // Düzeltme burada
                    newRepeatFlashcard.setTopic(topic);
                    newRepeatFlashcard.setRepeatTime(repeatTime);
                    return newRepeatFlashcard;
                });

        return repeatFlashcardRepository.save(repeatFlashcard);
    }
}
