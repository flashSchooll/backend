package com.flashcard.service;

import com.flashcard.controller.errorsupport.user.request.ErrorSupportSaveRequest;
import com.flashcard.model.Card;
import com.flashcard.model.ErrorSupport;
import com.flashcard.model.User;
import com.flashcard.repository.CardRepository;
import com.flashcard.repository.ErrorSupportRepository;
import com.flashcard.security.services.AuthService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ErrorSupportService {
    private final ErrorSupportRepository errorSupportRepository;
    private final CardRepository cardRepository;
    private final AuthService authService;

    @Transactional
    public void createErrorSupport(@Valid ErrorSupportSaveRequest errorSupport) {

        Card card = cardRepository.findById(errorSupport.cardId())
                .orElseThrow(() -> new EntityNotFoundException("Card not found"));

        User currentUser = authService.getCurrentUser();

        ErrorSupport support = new ErrorSupport();
        support.setCard(card);
        support.setUser(currentUser);
        support.setErrorMessage(errorSupport.errorMessage());
        support.setDeleted(false);

        errorSupportRepository.save(support);
    }

    public Page<ErrorSupport> getAll(Boolean isSolved, Pageable pageable) {
        return errorSupportRepository.findAllAsPage(isSolved, pageable);
    }

    public ErrorSupport get(Long id) {
        return errorSupportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ErrorSupport not found"));
    }

    @Transactional
    public ErrorSupport solve(Long id) {
        ErrorSupport errorSupport = get(id);
        errorSupport.setSolved(true);
        errorSupport.setSolvedDate(LocalDateTime.now());
        return errorSupportRepository.save(errorSupport);
    }

    @Transactional
    public void delete(Long id) {
        errorSupportRepository.deleteById(id);
    }
}
