package com.flashcard.service;

import com.flashcard.controller.tytflashcard.admin.request.TYTFlashcardSaveRequest;
import com.flashcard.controller.tytflashcard.admin.request.TYTFlashcardUpdateRequest;
import com.flashcard.controller.tytflashcard.admin.response.TYTFlashcardResponse;
import com.flashcard.model.TYTFlashcard;
import com.flashcard.model.TYTTopic;
import com.flashcard.repository.TYTFlashCardRepository;
import com.flashcard.repository.TYTTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TYTFlashCardService {

    private final TYTFlashCardRepository tytFlashCardRepository;
    private final TYTTopicRepository tytTopicRepository;

    @Transactional
    public TYTFlashcardResponse save(TYTFlashcardSaveRequest tytFlashcardSaveRequest) {
        Objects.requireNonNull(tytFlashcardSaveRequest.getTytTopicId());
        Objects.requireNonNull(tytFlashcardSaveRequest.getCardName());

        TYTTopic topic = tytTopicRepository.findById(tytFlashcardSaveRequest.getTytTopicId())
                .orElseThrow(() -> new NoSuchElementException("Konu bulunamadı"));

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
                .orElseThrow(() -> new NoSuchElementException("Flashcard bulunamadı"));

        flashcard.setCardName(tytFlashcardUpdateRequest.getCardName());

        flashcard = tytFlashCardRepository.save(flashcard);

        return new TYTFlashcardResponse(flashcard);
    }

    @Transactional
    public void delete(Long id) {
        Objects.requireNonNull(id);

        TYTFlashcard flashcard = tytFlashCardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Flashcard bulunamadı"));

        tytFlashCardRepository.delete(flashcard);
    }

    public List<TYTFlashcardResponse> getAll() {
        return tytFlashCardRepository.findAll().stream().map(TYTFlashcardResponse::new).toList();
    }
}
