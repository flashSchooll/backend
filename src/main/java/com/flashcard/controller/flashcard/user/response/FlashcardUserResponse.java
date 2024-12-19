package com.flashcard.controller.flashcard.user.response;

import com.flashcard.model.Flashcard;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FlashcardUserResponse {

    private final Long id;
    private final String topic;
    private final String cardName;
    private final Integer cardCount;
    private final boolean isSeen;

    public FlashcardUserResponse(Flashcard flashcard, Integer count, List<Long> flashcards) {
        boolean seen = flashcards.contains(flashcard.getId());

        this.id = flashcard.getId();
        this.topic = flashcard.getTopic().getSubject();
        this.cardName = flashcard.getCardName();
        this.cardCount = count;
        this.isSeen = seen;//todo kontrol et çalışmıyor
    }
}
