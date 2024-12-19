package com.flashcard.controller.flashcard.admin.response;

import com.flashcard.model.Flashcard;
import lombok.Getter;

@Getter
public class FlashcardResponse {

    private final Long id;
    private final String topic;
    private final String cardName;//todo seen eklenecek

    public FlashcardResponse(Flashcard flashcard) {
        this.id = flashcard.getId();
        this.topic = flashcard.getTopic().getSubject();
        this.cardName = flashcard.getCardName();
    }
}
