package com.flashcard.service;

import com.flashcard.controller.ownerflashcard.user.request.OwnerFlashcardSaveRequest;
import com.flashcard.controller.ownerflashcard.user.request.OwnerFlashcardUpdateRequest;
import com.flashcard.model.OwnerFlashcard;
import com.flashcard.model.User;
import com.flashcard.repository.OwnerFlashcardRepository;
import com.flashcard.security.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class OwnerFlashcardService {

    private final OwnerFlashcardRepository ownerFlashcardRepository;
    private final AuthService authService;

    @Transactional
    public OwnerFlashcard save(@Valid OwnerFlashcardSaveRequest request) {

        User user = authService.getCurrentUser();

        OwnerFlashcard ownerFlashcard = new OwnerFlashcard();
        ownerFlashcard.setName(request.getName());
        ownerFlashcard.setLesson(request.getYksLesson());
        ownerFlashcard.setUser(user);

        return ownerFlashcardRepository.save(ownerFlashcard);
    }

    @Transactional
    public OwnerFlashcard update(@Valid OwnerFlashcardUpdateRequest request) {

        OwnerFlashcard ownerFlashcard = ownerFlashcardRepository
                .findById(request.getId()).orElseThrow(() -> new NoSuchElementException("OwnerFlashcard bulunamadı"));

        ownerFlashcard.setName(request.getName());
        ownerFlashcard.setLesson(request.getYksLesson());

        return ownerFlashcardRepository.save(ownerFlashcard);
    }

    @Transactional
    public void delete(Long id) {
        OwnerFlashcard ownerFlashcard = ownerFlashcardRepository
                .findById(id).orElseThrow(() -> new NoSuchElementException("OwnerFlashcard bulunamadı"));

        ownerFlashcardRepository.delete(ownerFlashcard);
    }
}
