package com.flashcard.service;

import com.flashcard.controller.tytcard.admin.request.TYTCardSaveAllRequest;
import com.flashcard.controller.tytcard.admin.request.TYTCardSaveRequest;
import com.flashcard.controller.tytcard.admin.request.TYTCardUpdateRequest;
import com.flashcard.controller.tytcard.admin.response.TYTCardResponse;
import com.flashcard.model.TYTCard;
import com.flashcard.model.TYTFlashcard;
import com.flashcard.repository.TYTCardRepository;
import com.flashcard.repository.TYTFlashCardRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TYTCardService {

    private final TYTCardRepository tytCardRepository;
    private final TYTFlashCardService flashCardService;
    private final TYTFlashCardRepository tytFlashCardRepository;


    @Transactional
    public TYTCardResponse save(TYTCardSaveRequest tytCardSaveRequest) throws IOException {
        Objects.requireNonNull(tytCardSaveRequest.getTytFlashcardId());

        TYTFlashcard flashcard = tytFlashCardRepository.findById(tytCardSaveRequest.getTytFlashcardId())
                .orElseThrow(() -> new NoSuchElementException("Flashcard bulunamadı"));

        int countCartByFlashCard = tytCardRepository.countByTytFlashcard(flashcard);

        if (countCartByFlashCard > 20) {
            throw new BadRequestException("Bir Flashcartda 20 card olabilir");
        }

        TYTCard tytCard = new TYTCard();
        tytCard.setTytFlashcard(flashcard);
        tytCard.setBackFace(tytCardSaveRequest.getBackFace());
        tytCard.setFrontFace(tytCardSaveRequest.getFrontFace());
        tytCard.setDataBackFace(tytCardSaveRequest.getBackFile() != null ? tytCardSaveRequest.getBackFile().getBytes() : null);
        tytCard.setDataFrontFace(tytCardSaveRequest.getBackFile() != null ? tytCardSaveRequest.getFrontFile().getBytes() : null);

        tytCard = tytCardRepository.save(tytCard);

        return new TYTCardResponse(tytCard);
    }

    @Transactional
    public TYTCardResponse update(TYTCardUpdateRequest tytCardUpdateRequest) throws IOException {

        Objects.requireNonNull(tytCardUpdateRequest.getId());

        TYTCard tytCard = tytCardRepository.findById(tytCardUpdateRequest.getId())
                .orElseThrow(() -> new NoSuchElementException("Card bulunamadı"));

        tytCard.setBackFace(tytCardUpdateRequest.getBackFace());
        tytCard.setFrontFace(tytCardUpdateRequest.getFrontFace());
        tytCard.setDataBackFace(tytCardUpdateRequest.getBackFile() != null ? tytCardUpdateRequest.getBackFile().getBytes() : null);
        tytCard.setDataFrontFace(tytCardUpdateRequest.getBackFile() != null ? tytCardUpdateRequest.getFrontFile().getBytes() : null);

        tytCard = tytCardRepository.save(tytCard);

        return new TYTCardResponse(tytCard);
    }

    @Transactional
    public void delete(Long id) {

        Objects.requireNonNull(id);

        TYTCard tytCard = tytCardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Card bulunamadı"));

        tytCardRepository.delete(tytCard);
    }

    public List<TYTCardResponse> getAll(Long flashcardId) {
        Objects.requireNonNull(flashcardId);

        TYTFlashcard flashcard = tytFlashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException("Flashcard bulunamadı"));

        List<TYTCard> cards = tytCardRepository.findByTytFlashcard(flashcard);

        return cards.stream().map(TYTCardResponse::new).toList();

    }

    @Transactional
    public List<TYTCardResponse> saveAll(Long flashcardId, TYTCardSaveAllRequest request) throws IOException {
        Objects.requireNonNull(flashcardId);

        if (request.getTytCardSaveRequests().size() > 20) {
            throw new BadRequestException("Bir flashcartta en fazla 20 kart olabilir");
        }

        for (TYTCardSaveRequest saveRequest : request.getTytCardSaveRequests()) {
            save(saveRequest);
        }

        TYTFlashcard flashcard = tytFlashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException("Flashcard bulunamadı"));

        List<TYTCard> cardList = tytCardRepository.findByTytFlashcard(flashcard);

        return cardList.stream().map(TYTCardResponse::new).toList();
    }
}
