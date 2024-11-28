package com.flashcard.controller.tytflashcard.user.response;

import com.flashcard.model.TYTFlashcard;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TYTFlashcardUserResponse {

    private final Long id;
    private final String topic;
    private final String cardName;
    private final Integer cardCount;

    public TYTFlashcardUserResponse(TYTFlashcard flashcard, Integer count) {
        this.id = flashcard.getId();
        this.topic = flashcard.getTopic().getSubject();
        this.cardName = flashcard.getCardName();
        this.cardCount = count;
    }
}
