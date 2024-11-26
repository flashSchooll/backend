package com.flashcard.service;

import com.flashcard.controller.usercardseen.request.UserCardSeenRequest;
import com.flashcard.controller.usercardseen.request.UserCardSeenSaveRequest;
import com.flashcard.exception.BadRequestException;
import com.flashcard.model.TYTFlashcard;
import com.flashcard.model.User;
import com.flashcard.model.UserSeenCard;
import com.flashcard.repository.TYTCardRepository;
import com.flashcard.repository.TYTFlashCardRepository;
import com.flashcard.repository.UserCardSeenRepository;
import com.flashcard.security.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserCardSeenService {

    private final UserCardSeenRepository userCardSeenRepository;
    private final TYTFlashCardRepository tytFlashCardRepository;
    private final TYTCardRepository tytCardRepository;
    private final AuthService authService;
    private final UserCardPercentageService userCardPercentageService;

    @Transactional
    public void save(@Valid UserCardSeenSaveRequest userCardSeenSaveRequest) {

        TYTFlashcard flashcard = tytFlashCardRepository.findById(userCardSeenSaveRequest.getFlashcardId())
                .orElseThrow(() -> new NoSuchElementException("Flashcard bulunamadı"));

        int countCard = tytCardRepository.countByTytFlashcard(flashcard);

        if (countCard != userCardSeenSaveRequest.getUserCardSeenRequestList().size()) {
            throw new BadRequestException("Flashkarda ait bütün kartlar kaydedilmeli");
        }

        User user = authService.getCurrentUser();
        Duration duration = Duration.ofMinutes(userCardSeenSaveRequest.getMinute()).plusSeconds(userCardSeenSaveRequest.getSecond());

        List<UserSeenCard> cardList = new ArrayList<>();

        UserSeenCard userSeenCard;

        for (UserCardSeenRequest request : userCardSeenSaveRequest.getUserCardSeenRequestList()) {
            userSeenCard = new UserSeenCard();
            userSeenCard.setUser(user);
            userSeenCard.setStateOfKnowledge(request.getStateOfKnowledge());
            userSeenCard.setDifficultyLevel(request.getDifficultyLevel());
            userSeenCard.setDuration(duration);
            userSeenCard.setFlashcard(flashcard);

            cardList.add(userSeenCard);
        }
        userCardSeenRepository.saveAll(cardList);

        user.raiseRosette(1);
        user.raiseRosette(userCardSeenSaveRequest.getUserCardSeenRequestList().size());

        userCardPercentageService.updatePercentage(user, flashcard, userCardSeenSaveRequest.getUserCardSeenRequestList().size());
    }

    public List<UserSeenCard> getAllSeenCardsByFlashcard(Long flashcardId) {
        Objects.requireNonNull(flashcardId);

        TYTFlashcard flashcard = tytFlashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException("Flashcard bulunamadı"));
        User user = authService.getCurrentUser();

        return userCardSeenRepository.findByUserAndFlashcard(user, flashcard);
    }

    public List<UserSeenCard> getUnknownSeenCardsByFlashcard(Long flashcardId) {
        Objects.requireNonNull(flashcardId);

        TYTFlashcard flashcard = tytFlashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException("Flashcard bulunamadı"));
        User user = authService.getCurrentUser();

        return userCardSeenRepository.findByUserAndFlashcardAndStateOfKnowledgeIsFalse(user, flashcard);
    }

    public List<UserSeenCard> getKnownSeenCardsByFlashcard(Long flashcardId) {
        Objects.requireNonNull(flashcardId);

        TYTFlashcard flashcard = tytFlashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException("Flashcard bulunamadı"));
        User user = authService.getCurrentUser();

        return userCardSeenRepository.findByUserAndFlashcardAndStateOfKnowledgeIsTrue(user, flashcard);
    }
}
