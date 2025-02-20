package com.flashcard.service;

import com.flashcard.controller.ownercard.request.OwnerCardSaveRequest;
import com.flashcard.controller.ownercard.request.OwnerCardUpdateRequest;
import com.flashcard.model.OwnerFlashcard;
import com.flashcard.repository.OwnerCardRepository;
import com.flashcard.repository.OwnerFlashcardRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class OwnerCardService {

    private final OwnerCardRepository ownerCardRepository;
    private final OwnerFlashcardRepository ownerFlashcardRepository;

    @Transactional
    public OwnerCard save(@Valid OwnerCardSaveRequest request) {

        OwnerFlashcard ownerFlashcard = ownerFlashcardRepository
                .findById(request.getOwnerFlashcardId()).orElseThrow(() -> new NoSuchElementException("OwnerFlashcard bulunamadı"));

        int count = ownerCardRepository.countByOwnerFlashcard(ownerFlashcard);

        if (count > 19) {
            throw new IllegalArgumentException("Bir destede en fazla 20 kart olabilir");
        }

        OwnerCard ownerCard = new OwnerCard();
        ownerCard.setOwnerFlashcard(ownerFlashcard);
        ownerCard.setFrontFace(request.getFrontFace());
        ownerCard.setBackFace(request.getBackFace());

        return ownerCardRepository.save(ownerCard);
    }

    @Transactional
    public OwnerCard update(@Valid OwnerCardUpdateRequest request) {

        OwnerCard ownerCard = ownerCardRepository
                .findById(request.getId()).orElseThrow(() -> new NoSuchElementException("OwnerCard bulunamadı"));

        ownerCard.setFrontFace(request.getFrontFace());
        ownerCard.setBackFace(request.getBackFace());

        return ownerCardRepository.save(ownerCard);
    }

    @Transactional
    public void delete(Long id) {

        OwnerCard ownerCard = ownerCardRepository
                .findById(id).orElseThrow(() -> new NoSuchElementException("OwnerCard bulunamadı"));

        ownerCardRepository.delete(ownerCard);
    }

    public List<OwnerCard> getByOwnerFlashcard(Long ownerFlashcardId) {

        return ownerCardRepository.findByOwnerFlashcardId(ownerFlashcardId);
    }
}
