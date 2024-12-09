package com.flashcard.controller.flashcard.user.response;

import com.flashcard.model.Flashcard;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlashcardSearchResponse {

    private final String topic;
    private final Long topicId;
    private final String flashCardName;
    private final Long flashcardId;

    public FlashcardSearchResponse(Flashcard flashcard) {
        this.topic = flashcard.getTopic().getSubject();
        this.topicId = flashcard.getTopic().getId();
        this.flashCardName = flashcard.getCardName();
        this.flashcardId = flashcard.getId();
    }
}
