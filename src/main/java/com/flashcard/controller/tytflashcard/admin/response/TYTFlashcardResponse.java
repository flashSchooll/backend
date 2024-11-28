package com.flashcard.controller.tytflashcard.admin.response;

import com.flashcard.model.TYTFlashcard;
import lombok.Getter;

@Getter
public class TYTFlashcardResponse {

    private final Long id;
    private final String topic;
    private final String cardName;

    public TYTFlashcardResponse(TYTFlashcard flashcard) {
        this.id = flashcard.getId();
        this.topic = flashcard.getTopic().getSubject();
        this.cardName = flashcard.getCardName();
    }
}
