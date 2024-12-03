package com.flashcard.service;

import com.flashcard.constants.Constants;
import com.flashcard.model.MyCard;
import com.flashcard.model.TYTCard;
import com.flashcard.model.User;
import com.flashcard.repository.MyCardsRepository;
import com.flashcard.repository.TYTCardRepository;
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
    private final TYTCardRepository tytCardRepository;

    public TYTCard save(Long cardId) {
        Objects.requireNonNull(cardId);

        User user = authService.getCurrentUser();

        TYTCard tytCard = tytCardRepository.findById(cardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_CARD_NOT_FOUND));

        MyCard myCard = new MyCard();
        myCard.setTytCard(tytCard);
        myCard.setUser(user);

        myCardsRepository.save(myCard);

        return tytCard;
    }

    public void delete(Long cardId) {
        Objects.requireNonNull(cardId);

        User user = authService.getCurrentUser();

        TYTCard tytCard = tytCardRepository.findById(cardId)
                .orElseThrow(() -> new NoSuchElementException(Constants.TYT_CARD_NOT_FOUND));

        MyCard myCard = myCardsRepository.findByUserAndTytCard(user, tytCard)
                .orElseThrow(() -> new NoSuchElementException(Constants.MY_CARD_NOT_FOUND));

        myCardsRepository.delete(myCard);
    }

    public List<MyCard> getAll() {

        User user = authService.getCurrentUser();

        return myCardsRepository.findByUser(user);
    }
}
