package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.controller.usercardseen.request.UserSeenCardRequest;
import com.flashcard.controller.usercardseen.request.UserSeenCardSaveRequest;
import com.flashcard.exception.BadRequestException;
import com.flashcard.model.*;
import com.flashcard.repository.CardRepository;
import com.flashcard.repository.FlashCardRepository;
import com.flashcard.repository.TopicRepository;
import com.flashcard.repository.UserSeenCardRepository;
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
public class UserSeenCardService {

    private final UserSeenCardRepository userSeenCardRepository;
    private final FlashCardRepository flashCardRepository;
    private final CardRepository cardRepository;
    private final AuthService authService;
    private final UserCardPercentageService userCardPercentageService;
    private final TopicRepository topicRepository;
    private final DailyTargetService dailyTargetService;
    private final UserSeriesService userSeriesService;

    @Transactional
    public List<UserSeenCard> save(@Valid UserSeenCardSaveRequest userCardSeenSaveRequest) {

        Flashcard flashcard = flashCardRepository.findById(userCardSeenSaveRequest.getFlashcardId())
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));

        int countCard = cardRepository.countByFlashcard(flashcard);

        if (countCard != userCardSeenSaveRequest.getUserCardSeenRequestList().size()) {
            throw new BadRequestException(Constants.ALL_CARDS_ON_FLASHCARD_MUST_BE_SAVE);
        }

        User user = authService.getCurrentUser();

        int countFlashcard = userSeenCardRepository.countByUserAndCardFlashcard(user, flashcard);

        List<UserSeenCard> seenCards = new ArrayList<>();

        if (countFlashcard == 0) {
            Duration duration = Duration.ofMinutes(userCardSeenSaveRequest.getMinute()).plusSeconds(userCardSeenSaveRequest.getSecond());

            List<UserSeenCard> cardList = new ArrayList<>();

            UserSeenCard userSeenCard;

            for (UserSeenCardRequest request : userCardSeenSaveRequest.getUserCardSeenRequestList()) {
                Card card = cardRepository.findById(request.getCardId()).orElseThrow(() -> new NoSuchElementException(Constants.CARD_NOT_FOUND));

                userSeenCard = new UserSeenCard();
                userSeenCard.setUser(user);
                userSeenCard.setStateOfKnowledge(request.getStateOfKnowledge());
                userSeenCard.setDuration(duration);
                userSeenCard.setCard(card);

                cardList.add(userSeenCard);
            }
            userSeenCardRepository.saveAll(cardList);

            seenCards = cardList.stream().toList();

            user.raiseRosette();
            user.raiseStar(userCardSeenSaveRequest.getUserCardSeenRequestList().size());

            userCardPercentageService.updatePercentage(user, flashcard, userCardSeenSaveRequest.getUserCardSeenRequestList().size());
        }

        int cardCount = userCardSeenSaveRequest.getUserCardSeenRequestList().size();

        dailyTargetService.updateDailyTarget(cardCount,flashcard.getTopic().getLesson().getYks());

        return seenCards;
    }

    public List<UserSeenCard> getAllSeenCardsByFlashcard(Long flashcardId) {
        Objects.requireNonNull(flashcardId);

        Flashcard flashcard = flashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));
        User user = authService.getCurrentUser();

        return userSeenCardRepository.findByUserAndCardFlashcard(user, flashcard);
    }

    public List<UserSeenCard> getUnknownSeenCardsByFlashcard(Long flashcardId) {
        Objects.requireNonNull(flashcardId);

        Flashcard flashcard = flashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));
        User user = authService.getCurrentUser();

        return userSeenCardRepository.findByUserAndCardFlashcardAndStateOfKnowledgeIsFalse(user, flashcard);
    }

    public List<UserSeenCard> getKnownSeenCardsByFlashcard(Long flashcardId) {
        Objects.requireNonNull(flashcardId);

        Flashcard flashcard = flashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));
        User user = authService.getCurrentUser();

        return userSeenCardRepository.findByUserAndCardFlashcardAndStateOfKnowledgeIsTrue(user, flashcard);
    }

    public List<UserSeenCard> getAllSeenCardsByTopic(Long topicId) {
        Objects.requireNonNull(topicId);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TOPIC_NOT_FOUND));

        User user = authService.getCurrentUser();

        return userSeenCardRepository.findByUserAndCardFlashcardTopic(user, topic);
    }

    public List<Long> findFlashcardIdsByTopic(User user, Long topicId) {

        return userSeenCardRepository.findByUserAndCardFlashcardTopic(user, topicId);
    }
}
