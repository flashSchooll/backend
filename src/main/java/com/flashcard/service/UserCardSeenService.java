package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.usercardseen.request.UserCardSeenRequest;
import com.flashcard.controller.usercardseen.request.UserCardSeenSaveRequest;
import com.flashcard.exception.BadRequestException;
import com.flashcard.model.Card;
import com.flashcard.model.Flashcard;
import com.flashcard.model.User;
import com.flashcard.model.UserSeenCard;
import com.flashcard.repository.CardRepository;
import com.flashcard.repository.FlashCardRepository;
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
    private final FlashCardRepository flashCardRepository;
    private final CardRepository cardRepository;
    private final AuthService authService;
    private final UserCardPercentageService userCardPercentageService;

    @Transactional
    public List<UserSeenCard> save(@Valid UserCardSeenSaveRequest userCardSeenSaveRequest) {

        Flashcard flashcard = flashCardRepository.findById(userCardSeenSaveRequest.getFlashcardId())
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));

        int countCard = cardRepository.countByFlashcard(flashcard);

        if (countCard != userCardSeenSaveRequest.getUserCardSeenRequestList().size()) {
            throw new BadRequestException(Constants.ALL_CARDS_ON_FLASHCARD_MUST_BE_SAVE);
        }

        User user = authService.getCurrentUser();
        Duration duration = Duration.ofMinutes(userCardSeenSaveRequest.getMinute()).plusSeconds(userCardSeenSaveRequest.getSecond());

        List<UserSeenCard> cardList = new ArrayList<>();

        UserSeenCard userSeenCard;

        for (UserCardSeenRequest request : userCardSeenSaveRequest.getUserCardSeenRequestList()) {
            Card card = cardRepository.findById(request.getCardId()).orElseThrow(() -> new NoSuchElementException(Constants.TYT_CARD_NOT_FOUND));

            userSeenCard = new UserSeenCard();
            userSeenCard.setUser(user);
            userSeenCard.setStateOfKnowledge(request.getStateOfKnowledge());
            userSeenCard.setDuration(duration);
            userSeenCard.setCard(card);

            cardList.add(userSeenCard);
        }
        userCardSeenRepository.saveAll(cardList);

        List<UserSeenCard> seenCards = cardList.stream().toList();

        user.raiseRosette();
        user.raiseStar(userCardSeenSaveRequest.getUserCardSeenRequestList().size());

        userCardPercentageService.updatePercentage(user, flashcard, userCardSeenSaveRequest.getUserCardSeenRequestList().size());

        return seenCards;
    }

    public List<UserSeenCard> getAllSeenCardsByFlashcard(Long flashcardId) {
        Objects.requireNonNull(flashcardId);

        Flashcard flashcard = flashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));
        User user = authService.getCurrentUser();

        return userCardSeenRepository.findByUserAndCardFlashcard(user, flashcard);
    }

    public List<UserSeenCard> getUnknownSeenCardsByFlashcard(Long flashcardId) {
        Objects.requireNonNull(flashcardId);

        Flashcard flashcard = flashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));
        User user = authService.getCurrentUser();

        return userCardSeenRepository.findByUserAndCardFlashcardAndStateOfKnowledgeIsFalse(user, flashcard);
    }

    public List<UserSeenCard> getKnownSeenCardsByFlashcard(Long flashcardId) {
        Objects.requireNonNull(flashcardId);

        Flashcard flashcard = flashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));
        User user = authService.getCurrentUser();

        return userCardSeenRepository.findByUserAndCardFlashcardAndStateOfKnowledgeIsTrue(user, flashcard);
    }
}
