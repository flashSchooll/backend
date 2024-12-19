package com.flashcard.controller.flashcard.user.response;

import com.flashcard.model.Flashcard;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FlashcardUserResponse {

    private final Long id;
    private final Long topicId;
    private final String topic;
    private final String cardName;
    private final Integer cardCount;
    private final boolean isSeen;

    public FlashcardUserResponse(Flashcard flashcard, Integer count, boolean seen) {

        this.id = flashcard.getId();
        this.topicId = flashcard.getTopic().getId();
        this.topic = flashcard.getTopic().getSubject();
        this.cardName = flashcard.getCardName();
        this.cardCount = count;
        this.isSeen = seen;
    }
}
