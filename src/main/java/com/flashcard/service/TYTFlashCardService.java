package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.tytflashcard.admin.request.TYTFlashcardSaveRequest;
import com.flashcard.controller.tytflashcard.admin.request.TYTFlashcardUpdateRequest;
import com.flashcard.controller.tytflashcard.admin.response.TYTFlashcardResponse;
import com.flashcard.controller.tytflashcard.user.response.TYTFlashcardUserResponse;
import com.flashcard.controller.tyttopic.user.response.TYTTopicUserResponse;
import com.flashcard.exception.BadRequestException;
import com.flashcard.model.TYTCard;
import com.flashcard.model.TYTFlashcard;
import com.flashcard.model.TYTTopic;
import com.flashcard.repository.TYTCardRepository;
import com.flashcard.repository.TYTFlashCardRepository;
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
public class TYTFlashCardService {

    private final TYTFlashCardRepository tytFlashCardRepository;
    private final TYTTopicRepository tytTopicRepository;
    private final TYTCardRepository tytCardRepository;

    @Transactional
    public TYTFlashcardResponse save(TYTFlashcardSaveRequest tytFlashcardSaveRequest) {
        Objects.requireNonNull(tytFlashcardSaveRequest.getTopicId());
        Objects.requireNonNull(tytFlashcardSaveRequest.getCardName());

        TYTTopic topic = tytTopicRepository.findById(tytFlashcardSaveRequest.getTopicId())
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_TOPIC_NOT_FOUND));

        boolean isExist = tytFlashCardRepository.existsByCardName(tytFlashcardSaveRequest.getCardName());

        if (isExist) {
            throw new BadRequestException(Constants.FLASHCARD_NAME_ALREADY_EXISTS);
        }

        TYTFlashcard flashcard = new TYTFlashcard();
        flashcard.setTopic(topic);
        flashcard.setCardName(tytFlashcardSaveRequest.getCardName());

        flashcard = tytFlashCardRepository.save(flashcard);

        return new TYTFlashcardResponse(flashcard);
    }

    @Transactional
    public TYTFlashcardResponse update(TYTFlashcardUpdateRequest tytFlashcardUpdateRequest) {
        Objects.requireNonNull(tytFlashcardUpdateRequest.getFlashcardId());
        Objects.requireNonNull(tytFlashcardUpdateRequest.getCardName());

        TYTFlashcard flashcard = tytFlashCardRepository.findById(tytFlashcardUpdateRequest.getFlashcardId())
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));

        boolean isExist = tytFlashCardRepository.existsByCardName(tytFlashcardUpdateRequest.getCardName());

        if (isExist) {
            throw new BadRequestException(Constants.FLASHCARD_NAME_ALREADY_EXISTS);
        }

        flashcard.setCardName(tytFlashcardUpdateRequest.getCardName());

        flashcard = tytFlashCardRepository.save(flashcard);

        return new TYTFlashcardResponse(flashcard);
    }

    @Transactional
    public void delete(Long id) {
        Objects.requireNonNull(id);

        TYTFlashcard flashcard = tytFlashCardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));

        tytFlashCardRepository.delete(flashcard);
    }

    public List<TYTFlashcard> getAll(Long topicId) {
        Objects.requireNonNull(topicId);

        TYTTopic topic = tytTopicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_TOPIC_NOT_FOUND));

        return tytFlashCardRepository.findByTopic(topic);
    }

    public Map<String, Long> getAllByLesson() {

        List<TYTFlashcard> flashcards = tytFlashCardRepository.findAll();

        Map<String, Long> flashcardsGroupedByLesson = flashcards.stream()
                // Her bir flashcard'ı lesson'a göre gruplandırıyoruz
                .collect(Collectors.groupingBy(
                        flashcard -> flashcard.getTopic().getTytLesson().getTyt().label,  // lesson ismi
                        Collectors.counting()  // lesson başına kaç flashcard var
                ));

        return flashcardsGroupedByLesson;
    }

    public List<TYTFlashcardUserResponse> getAllUser(Long topicId) {
        Objects.requireNonNull(topicId);

        TYTTopic topic = tytTopicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_TOPIC_NOT_FOUND));

        List<TYTCard> tytCards = tytCardRepository.findByTYTTopic(topic);

        Map<TYTFlashcard, Long> cardCount = tytCards.stream()
                .collect(Collectors.groupingBy(
                        TYTCard::getTytFlashcard,
                        Collectors.counting()
                ));

        return tytFlashCardRepository.findByTopic(topic)
                .stream()
                .map(flashcard -> new TYTFlashcardUserResponse(flashcard, Math.toIntExact(cardCount.get(flashcard))))
                .toList();
    }

    public List<TYTFlashcardResponse> search(String search) {

        List<TYTFlashcard> flashcards = tytFlashCardRepository.search(search);

        return flashcards.stream().map(TYTFlashcardResponse::new).toList();
    }
}
