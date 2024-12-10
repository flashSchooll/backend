package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.exception.BadRequestException;
import com.flashcard.model.Flashcard;
import com.flashcard.model.MyCard;
import com.flashcard.model.Card;
import com.flashcard.model.User;
import com.flashcard.model.enums.DifficultyLevel;
import com.flashcard.repository.FlashCardRepository;
import com.flashcard.repository.MyCardsRepository;
import com.flashcard.repository.CardRepository;
import com.flashcard.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MyCardsService {

    private final MyCardsRepository myCardsRepository;
    private final AuthService authService;
    private final CardRepository cardRepository;
    private final FlashCardRepository flashCardRepository;

    public Card save(Long cardId) {
        Objects.requireNonNull(cardId);

        User user = authService.getCurrentUser();

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_CARD_NOT_FOUND));

        boolean isExists = myCardsRepository.existsByUserAndCard(user, card);

        if (isExists) {
            throw new BadRequestException("Kart zaten kayıtlı");
        }

        MyCard myCard = new MyCard();
        myCard.setCard(card);
        myCard.setUser(user);

        myCardsRepository.save(myCard);

        return card;
    }

    public void delete(Long cardId) {
        Objects.requireNonNull(cardId);

        User user = authService.getCurrentUser();

        Card tytCard = cardRepository.findById(cardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_CARD_NOT_FOUND));

        MyCard myCard = myCardsRepository.findByUserAndCard(user, tytCard)
                .orElseThrow(() -> new NoSuchElementException(Constants.MY_CARD_NOT_FOUND));

        myCardsRepository.delete(myCard);
    }

    public List<MyCard> getAll(DifficultyLevel difficultyLevel) {

        User user = authService.getCurrentUser();

        return myCardsRepository.findByUser(user, difficultyLevel);
    }

    public List<MyCard> getAll(Long flashcardId) {

        User user = authService.getCurrentUser();
        Flashcard flashcard = flashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.FLASHCARD_NOT_FOUND));

        return myCardsRepository.findByUserAndCardFlashcard(user, flashcard);
    }

    public MyCard saveWithLevel(Long cardId, String difficultyLevel) {
        Objects.requireNonNull(cardId);

        User user = authService.getCurrentUser();

        Card tytCard = cardRepository.findById(cardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_CARD_NOT_FOUND));
        DifficultyLevel newDifficultyLevel = DifficultyLevel.by(difficultyLevel)
                .orElseThrow(() -> new NoSuchElementException("Zorluk derecesi bulunamadı"));

        MyCard myCard = new MyCard();
        myCard.setCard(tytCard);
        myCard.setUser(user);
        myCard.setDifficultyLevel(newDifficultyLevel);

        return myCardsRepository.save(myCard);
    }
}
